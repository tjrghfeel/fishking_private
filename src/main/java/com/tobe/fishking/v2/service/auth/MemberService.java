package com.tobe.fishking.v2.service.auth;


import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.common.Take;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.exception.*;
import com.tobe.fishking.v2.model.auth.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import lombok.AllArgsConstructor;


import org.springframework.core.env.Environment;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService {

    private MemberRepository memberRepository;
    private PostRepository postRepository;
    private TakeRepository takeRepository;
    private GoodsRepository goodsRepository;
    private LoveToRepository loveToRepository;
    private ShipRepository shipRepository;
    private FileRepository fileRepository;
    private FishingDiaryRepository fishingDiaryRepository;
    private ReviewRepository reviewRepository;
    private FishingDiaryCommentRepository fishingDiaryCommentRepository;
    private CouponMemberRepository couponMemberRepository;
    private UploadService uploadService;
    private PasswordEncoder encoder;
    private PhoneAuthRepository phoneAuthRepository;
    private Environment env;

    public Member getMemberBySessionToken(final String sessionToken) {
        final Optional<Member> optionalMember =  memberRepository.findBySessionToken(sessionToken);
        optionalMember.orElseThrow(() -> new CMemberExistException(sessionToken));
        return optionalMember.get();
//        return optionalMember.isEmpty() ? null : optionalMember.get();
    }

    public long getMemberSeqBySessionToken(final String sessionToken) {
        final Optional<Member> optionalMember =  memberRepository.findBySessionToken(sessionToken);
        optionalMember.orElseThrow(() -> new CMemberExistException(sessionToken));
        return optionalMember.get().getId();
    }

    /*회원가입 - 휴대폰 인증
    * - 폰번호 중복 확인
    * - 실제 있는폰번호인지 확인
    * - 인증문자 전송
    * - 반환 ) 이미 가입된 번호이면 예외. 문자송신 성공하면 true, 실패하면 false. */
    @Transactional
    public boolean smsAuthForSignUp(String areaCode, String localNumber){
        if(checkExistByPhoneNum(areaCode,localNumber)){//이미 가입된 휴대폰번호이면
            throw new PhoneNumberDupException("이미 회원가입한 번호입니다");
        }
        else{
            if(requestSmsAuth(areaCode,localNumber)==true){return true;}//문자 전송이 성공한다면,
            else return false;
        }
    }

    /*회원가입
    * - 이메일 중복 확인
    * - 비번 '자바 암호화' (그 외 개인정보 암호화는 db저장시 jpa converter에서 수행)
    * - Member생성하여 db에 저장.
    * - 회원정보와 문자인증 정보가 함께 넘어옴. */
    public boolean signUp(SignUpDto signUpDto){
        /*이메일 중복 확인*/
        if(memberRepository.existsByEmail(signUpDto.getEmail())==true){
            /*!!!!!예외처리 이렇게하는게 맞는지? 그냥 던지기만하면 프론트에서 알아서 처리하는지? */
            throw new EmailDupException("이메일이 중복됩니다");
        }

        /*회원 정보 저장*/
        /*전화번호를 나타내는 PhoneNumber생성*/
        PhoneNumber phoneNumber = new PhoneNumber(signUpDto.getAreaCode(),signUpDto.getLocalNumber());
        /*비밀번호 자바 암호화 및 개인정보 db암호화*/
        String encodedPw = encoder.encode(signUpDto.getPw());

        /*Member 저장*/
        Member member = Member.builder()
                .uid(signUpDto.getEmail())//!!!!!uid가 뭔지몰라 일단 중복처리를 하기때문에 똑같이 유일한 이메일로 설정.
//                .memberName()
                .nickName(signUpDto.getNickName())
                .password(encodedPw)
                .email(signUpDto.getEmail())//일단 이렇게.
                .gender(Gender.boy)
                .roles(Role.member)
//                .profileImage()
                .isActive(true)
//                .certifiedNo()
                .isCertified(true)
//                .joinDt()
//                .snsType()
//                .snsId()
//                .statusMessage()
//                .address()
                .phoneNumber(phoneNumber)
                .build();

        memberRepository.save(member).getId();
        return true;
    }

    /*휴대폰 번호로 가입한 회원 존재유무 확인
    * - 존재하면 true반환. */
    @Transactional
    public boolean checkExistByPhoneNum(String areaCode, String localNumber){
        if(memberRepository.findByAreaCodeAndLocalNumber(areaCode, localNumber)!=null){
            return true;
        }
        else return false;
    }

    /*문자인증 요청
    * - 랜덤한 인증번호 생성.
    * - 인증번호가 담긴 sms 문자인증 메세지를 pNum으로 발송.
    * - pNum과 인증번호를 db에 저장.
    * - 반환 ) 전송 실패시 false. 성공시 true. */
    @Transactional
    public boolean requestSmsAuth(String areaCode, String localNumber){
        /*랜덤으로 인증번호 생성.*/
        String randomNum=null;
        randomNum = String.format("%04d",((int)(Math.random()*10000)));

        /*인증번호와 폰번호 db에 저장. */
        PhoneAuth phoneAuth = PhoneAuth.builder()
                .phoneNumber(new PhoneNumber(areaCode,localNumber))
                .certifyNum(randomNum)
                .build();

        phoneAuthRepository.save(phoneAuth);

        /*!!!!! 문자 전송. */

        return true;
    }

    /*문자인증 확인 메소드
    * - 넘어온 폰번호와 인증번호를 가지고 테이블상의 값과 일치하는지 확인.
    * - 일치하면 true,아니면 false
    * - 반환 ) 인증번호가 맞으면 true, 틀리면 false. */
    @Transactional
    public boolean checkSmsAuth(String areaCode, String localNumber, String authNum){
        /*폰번호에 해당하는 인증번호 가져옴*/
        PhoneAuth phoneAuth = phoneAuthRepository.findByAreaCodeAndLocalNumber(areaCode,localNumber);
        if(phoneAuth.getCertifyNum().equals(authNum)){
            phoneAuthRepository.delete(phoneAuth);
            return true;
        }
        else return  false;
    }

    /*비번변경 메소드
    * - 비번암호화하여 세션토큰에 해당하는 멤버의 비번필드에 update. */
    @Transactional
    public boolean updatePw(ResetPwDto resetPwDto) {
        /*비번 암호화*/
//        String encodedPw = encoder.encode(resetPwDto.getNewPw());
        String encodedPw = resetPwDto.getNewPw();


        /*세션토큰에 해당하는 멤버의 비번필드 업데이트*/
        Member member = memberRepository.findByAreaCodeAndLocalNumber(resetPwDto.getAreaCode(),resetPwDto.getLocalNumber());

        member.setPassword(encodedPw);

        return true;
    }


    /*로그인
     * - 아디, 비번확인하고 아니면 예외처리??, 맞으면 세션토큰생성하여 저장하고 반환. */
    @Transactional
    public String login(LoginDTO loginDTO){
        String sessionToken=null;
        /*아디,비번 확인*/
        Member member = memberRepository.findByEmail(loginDTO.getMemberId());
        if(member==null){
            throw new IncorrectIdException("아이디가 존재하지 않습니다");
        }
        else if(encoder.matches(loginDTO.getPassword(),member.getPassword())){//로그인 성공
            /*세션토큰이 이미존재한다면. 즉, 이미 로그인되어있는 회원이라면 기존의 세션토큰을 반환해줌. */
            if(member.getSessionToken()!=null){
                return member.getSessionToken();
            }
            else {
                /*세션토큰 생성 및 저장. */
                String rawToken = member.getEmail() + LocalDateTime.now();
                sessionToken = encoder.encode(rawToken);

                member.setSessionToken(sessionToken);
            }
        }
        else{throw new IncorrectPwException("비밀번호가 잘못되었습니다");}
        return sessionToken;
    }

    /*로그아웃
    * - 세션토큰에 해당하는 멤버 찾아서 세션토큰필드 비워줌. */
    @Transactional
    public boolean logout(String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        member.setSessionToken(null);
        return  true;
    }

    /*사용자 프로필 보기
    * - 해당 사용자가 본인인지, 일반 다른사용자인지, 업체인지에 따라 DTO에 필요한 정보를 추가해주는식으로.  */
    @Transactional
    public UserProfileDTO getUserProfile(Long profileUserId, String sessionToken) throws ResourceNotFoundException {
        /*repository로부터 필요한 정보 가져오기*/
            /*userId로부터 Member를 가져온다. */
            Member member = memberRepository.findById(profileUserId)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+profileUserId));
            Member me = memberRepository.findBySessionToken(sessionToken)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken :: "+sessionToken));

            /*비활성화된 멤버라면 빈 dto를 반환. */
            if(member.getIsActive()==false){
                UserProfileDTO dto = new UserProfileDTO();
                dto.setIsActive(false);
                return dto;
            }
            /*DTO저장에 필요한 작성글수(fishingDiary글만을 취급, 좋아요수, 업체찜수를 가져온다. */
            int postCount = fishingDiaryRepository.countByMember(member);
            int takeCount = goodsRepository.findTakeCount(member);//!!!!!어떤것에 대한 찜 수인지 확인후 수정. 일단은 상품에 대한 찜 수.
            //좋아요 수???!!!!! member에 대한 좋아요가 뭔지 확인하고 추가필요.

        /*가져온 데이터들을 UserProfileDTO에 저장. (본인이 아닌 경우, '업체찜수'를 추가해준다)  */
            /*공통적으로 필요한 프로필정보 dto에 추가. */
            UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .memberId(member.getId())
                .nickName((member.getNickName()==null)?("이름없음"):(member.getNickName()))
                .profileImage(env.getProperty("file.downloadUrl") + member.getProfileImage())
                .isActive(member.getIsActive())
                .postCount(postCount)
                .takeCount(takeCount)
                .isMe(false)
                .isShip(false)
                //.likeCount() !!!!! member에 대한 좋아요는 뭘말하는지 모호해 일단 생략. 수정 필요.
                .build();

            /*본인인 경우를 표시. */
            if(profileUserId.equals(me.getId())){ userProfileDTO.setIsMe(true); }

            /*업체회원인 경우 정보추가  */
            Ship ship = shipRepository.findByMember(member);
            if(ship!=null){//Role이 선주인 member가 ship이 무조건 있는건지 없는건지 모르겠어서 확인차 추가.
                List<String> fishSpeciesString = new LinkedList<String>();
                List<CommonCode> fishSpecies = ship.getFishSpecies();
                for(int i=0; i<fishSpecies.size(); i++){
                    fishSpeciesString.add(fishSpecies.get(i).getCodeName());
                }
                int likeCount = loveToRepository.countByTakeTypeAndLinkId(TakeType.ship, ship.getId());//ship이 받은 좋아요 수 카운트.

                userProfileDTO.setIsShip(true);
                userProfileDTO.setLikeCount(likeCount);
                userProfileDTO.setShipId(ship.getId());
                userProfileDTO.setShipName(ship.getShipName());//!!!!!ShipOwner role의 member의 이름이 ship의 이름인지 잘모르겠음.
                userProfileDTO.setSido(ship.getSido());
                userProfileDTO.setFishSpecies(fishSpeciesString);
            }

        return userProfileDTO;
    }

    /*프로필 관리 페이지 보기
    * - member의 프로필이미지, uid, nickName, 상태메세지, 휴대폰번호, 이메일 정보가 든 dto반환.*/
    @Transactional
    public ProfileManageDTO getProfileManage(String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        ProfileManageDTO profileManageDTO = ProfileManageDTO.builder()
                .id(member.getId())
                .uid(member.getUid())
                .nickName((member.getNickName()==null)?("없음"):(member.getNickName()))
                .email(member.getEmail())
                .profileImage(env.getProperty("file.downloadUrl")+member.getProfileImage())
                .statusMessage((member.getStatusMessage()==null)?("없음"):(member.getStatusMessage()))
                //!!!!!아래 전화번호는 nullable필드이지만 회원가입시 휴대폰인증을 하므로 무조건 있다고 판단.
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .build();

        return profileManageDTO;
    }

    /*프사변경*/
    @Transactional
    public boolean updateProfileImage(MultipartFile file, String sessionToken) throws Exception {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*기존 프사 fileEntity 삭제*/
        FileEntity preFileEntity = fileRepository.findTop1ByPidAndFilePublishAndIsRepresent(member.getId(), FilePublish.profile,true);
        if(preFileEntity!=null){  uploadService.removeFileEntity(preFileEntity.getId());}

        if(uploadService.checkFileType(file)!=FileType.image){
            throw new Exception();//!!!!!어떤 예외 던져야할지.
        }
        /*프사 추가 및 Member의 profileImage 업데이트. */
        Map<String,Object> fileInfo = uploadService.initialFile(file, FilePublish.profile, "");

        FileEntity fileEntity = FileEntity.builder()
                .pid(member.getId())
                .originalFile(file.getOriginalFilename())
                .fileName(file.getOriginalFilename())
                .fileNo(0)
                .filePublish(FilePublish.profile)
                .fileType(FileType.image)
                .fileUrl((String)fileInfo.get("path"))
//                .downloadUrl((String)fileInfo.get("fileDownloadUrl"))
                .thumbnailFile((String)fileInfo.get("thumbnailName"))
//                .downloadThumbnailUrl((String)fileInfo.get("thumbDownloadUrl"))
                .size(file.getSize())
                .storedFile((String)fileInfo.get("fileName"))
                .isRepresent(true)
                .createdBy(member)
                .modifiedBy(member)
                .locations("sampleLocation")
                .build();
        fileRepository.save(fileEntity);

        member.setProfileImage("/"+fileInfo.get("path")+"/"+fileInfo.get("thumbnailName"));

        return true;
    }

    /*닉네임 변경*/
    @Transactional
    public String modifyProfileNickName(String sessionToken, ModifyingNickNameDto nickName) throws ResourceNotFoundException {
        /*Member 가져옴*/
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*닉네임 업데이트*/
        member.setNickName(nickName.getNickName());
        return member.getNickName();
    }

    /*상태 메세지 변경*/
    @Transactional
    public String modifyProfileStatusMessage(String sessionToken, ModifyingStatusMessageDto statusMessage) throws ResourceNotFoundException {
        /*Member 가져옴*/
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*닉네임 업데이트*/
        member.setStatusMessage(statusMessage.getStatusMessage());
        return member.getStatusMessage();
    }

    /*이메일 변경*/
    @Transactional
    public String modifyProfileEmail(String sessionToken, ModifyingEmailDto email) throws ResourceNotFoundException {
        /*!!!!!유효성 체크 로직. (만약 서비스계층에서 수행해야하는거면)*/

        /*Member 가져옴*/
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*닉네임 업데이트*/
        member.setEmail(email.getEmail());
        return member.getEmail();
    }

    /*비번 변경*/
    @Transactional
    public boolean modifyProfilePassword(String sessionToken, String currentPw, String newPw) throws ResourceNotFoundException {
        /*currentPw가 맞는지 확인. 맞으면 pw변경, 아니면 false반환. */
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        //비번변경.
        if(encoder.matches(currentPw, member.getPassword())){
            member.setPassword(encoder.encode(newPw));
            return true;
        }
        else return false;
    }

    /*탈퇴하기
    * - 해당 회원의 예약건 취소
    * - 글들을 비활성화 처리한다. 좋아요,찜,스크랩,쿠폰 삭제 및 리뷰,post,fishingDiary,fishingDiaryComment 비활성화.
    * - !!!!!탈퇴회원이 업주일 경우 어떻게 처리할지는 업주쪽 기획이 나온뒤 결정. */
    @Transactional
    public Long inactivateMember(String sessionToken) throws ResourceNotFoundException {
        /*Member의 isActive를 false로 변경*/
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*member 비활성화. */
        member.deActivateMember();

        /*예약 취소처리 !!!!!orders엔터티 완성되면 구현. */

        /*게시글들 비활성화 처리. */
            /*좋아요,찜,스크랩,쿠폰 삭제 */
            /* 탈퇴하는 회원의 모든 글 등에 대해서 isActive 필드를 부여하고 false값을 주는 방식으로 처리하게되면 처리량이 너무
                많아진다고 하심. 그냥 member에다가만 isActive필드를 부여하고 글 조회하는쪽 로직에서 member의 isActive로 처리하는게
                낫다고 하심. 좋아요,찜,스크랩,쿠폰도 그냥 지우지말자.
            List<LoveTo> loveTo = loveToRepository.findByCreatedBy(member);
            loveToRepository.deleteAll(loveTo);

            List<Take> take = takeRepository.findByCreatedBy(member);
            takeRepository.deleteAll(take);

            fishingDiaryRepository.deleteAllScrapByMember(member);

            List<CouponMember> couponMembers = couponMemberRepository.findByMember(member);
            couponMemberRepository.deleteAll(couponMembers);

            *//*리뷰,post,fishingDiary,fishingDiaryComment 비활성화 *//*
            fishingDiaryRepository.updateIsActiveByMember(member);
            postRepository.updateIsActiveByMember(member);
            fishingDiaryCommentRepository.updateIsActiveByMember(member);
            reviewRepository.updateIsActiveByMember(member);
*/
        return member.getId();
    }

}
