package com.tobe.fishking.v2.service.auth;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.exception.CMemberExistException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.auth.ProfileManageDTO;
import com.tobe.fishking.v2.model.auth.UserProfileDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.LoveToRepository;
import com.tobe.fishking.v2.repository.common.TakeRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.LinkedList;
import java.util.List;
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

    /*사용자 프로필 보기
    * - 해당 사용자가 본인인지, 일반 다른사용자인지, 업체인지에 따라 DTO에 필요한 정보를 추가해주는식으로.  */
    @Transactional
    public UserProfileDTO getUserProfile(Long profileUserId, Long myId) throws ResourceNotFoundException {
        /*repository로부터 필요한 정보 가져오기*/
            /*userId로부터 Member를 가져온다. */
            Member member = memberRepository.findById(profileUserId)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+profileUserId));
            /*DTO저장에 필요한 작성글수(fishingDiary글만을 취급, 좋아요수, 업체찜수를 가져온다. */
            int postCount = fishingDiaryRepository.countByMember(member);
            int takeCount = goodsRepository.findTakeCount(member);//!!!!!어떤것에 대한 찜 수인지 확인후 수정. 일단은 상품에 대한 찜 수.
            //좋아요 수???!!!!!! member에 대한 좋아요가 뭔지 확인하고 추가필요.

        /*가져온 데이터들을 UserProfileDTO에 저장. (본인이 아닌 경우, '업체찜수'를 추가해준다)  */
            /*공통적으로 필요한 프로필정보 dto에 추가. */
            UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .memberId(member.getId())
                .nickName((member.getNickName()==null)?("이름없음"):(member.getNickName()))
                .profileImage(member.getProfileImage())
                .isActive(member.getIsActive())
                .postCount(postCount)
                .takeCount(takeCount)
                .isMe(false)
                .isShip(false)
                //.likeCount() !!!!! member에 대한 좋아요는 뭘말하는지 모호해 일단 생략. 수정 필요.
                .build();

            /*본인인 경우를 표시. */
            if(profileUserId.equals(myId)){ userProfileDTO.setIsMe(true); }

            /*업체회원인 경우 정보추가  */
            Ship ship = shipRepository.findByMember(member);
            if(ship!=null){//Role이 선주인 member가 ship이 무조건 있는건지 없는건지 모르겠어서 확인차 추가.
                List<String> fishSpeciesString = new LinkedList<String>();
                List<CommonCode> fishSpecies = ship.getFishSpecies();
                for(int i=0; i<fishSpecies.size(); i++){
                    fishSpeciesString.add(fishSpecies.get(i).getCodeName());
                }
                int likeCount = loveToRepository.countByTaskTypeAndLinkId(TakeType.ship, ship.getId());//ship이 받은 좋아요 수 카운트.

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
    public ProfileManageDTO getProfileManage(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        ProfileManageDTO profileManageDTO = ProfileManageDTO.builder()
                .id(member.getId())
                .uid(member.getUid())
                .nickName((member.getNickName()==null)?("없음"):(member.getNickName()))
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .statusMessage((member.getStatusMessage()==null)?("없음"):(member.getStatusMessage()))
                //!!!!!아래 전화번호는 nullable필드이지만 회원가입시 휴대폰인증을 하므로 무조건 있다고 판단.
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .build();

        return profileManageDTO;
    }

    /*닉네임 변경*/
    @Transactional
    public String modifyProfileNickName(Long memberId, String nickName) throws ResourceNotFoundException {
        /*Member 가져옴*/
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*닉네임 업데이트*/
        member.setNickName(nickName);
        return member.getNickName();
    }

    /*상태 메세지 변경*/
    @Transactional
    public String modifyProfileStatusMessage(Long memberId, String statusMessage) throws ResourceNotFoundException {
        /*Member 가져옴*/
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*닉네임 업데이트*/
        member.setStatusMessage(statusMessage);
        return member.getStatusMessage();
    }

    /*이메일 변경*/
    @Transactional
    public String modifyProfileEmail(Long memberId, String email) throws ResourceNotFoundException {
        /*!!!!!!!유효성 체크 로직. (만약 서비스계층에서 수행해야하는거면)*/

        /*Member 가져옴*/
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*닉네임 업데이트*/
        member.setEmail(email);
        return member.getEmail();
    }

    /*비번 변경*/
    @Transactional
    public boolean modifyProfilePassword(Long memberId, String currentPw, String newPw) throws ResourceNotFoundException {
        /*currentPw가 맞는지 확인. 맞으면 pw변경, 아니면 false반환. */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        //!!!!!비번이 입력데이터 그대로 db에 저장되는거면 변경필요없지만, 그게아니라면 수정 필요.
        if(member.getPassword().equals(currentPw)){
            member.setPassword(newPw);
            return true;
        }
        else return false;
    }

    /*탈퇴하기*/
    @Transactional
    public Long deleteMember(Long memberId) throws ResourceNotFoundException {
        /*Member의 isActive를 false로 변경*/
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        member.deActivateMember();
        return member.getId();
    }

}
