package com.tobe.fishking.v2.service.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.*;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.*;
import com.tobe.fishking.v2.model.auth.*;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.AES;
import lombok.AllArgsConstructor;


import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final TakeRepository takeRepository;
    private final GoodsRepository goodsRepository;
    private final LoveToRepository loveToRepository;
    private final ShipRepository shipRepository;
    private final FileRepository fileRepository;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final ReviewRepository reviewRepository;
    private final FishingDiaryCommentRepository fishingDiaryCommentRepository;
    private final CouponMemberRepository couponMemberRepository;
    private final UploadService uploadService;
    private final PasswordEncoder encoder;
    private final PhoneAuthRepository phoneAuthRepository;
    private final Environment env;
    private final CommonCodeRepository commonCodeRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final TblSubmitQueueRepository tblSubmitQueueRepository;
    private final CompanyRepository companyRepository;

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
  /*  @Transactional
    public boolean smsAuthForSignUp(String areaCode, String localNumber){
        if(checkExistByPhoneNum(areaCode,localNumber)){//이미 가입된 휴대폰번호이면
            throw new PhoneNumberDupException("이미 회원가입한 번호입니다");
        }
        else{
            if(requestSmsAuth(areaCode,localNumber)==true){return true;}//문자 전송이 성공한다면,
            else return false;
        }
    }*/

    /*회원가입 - 문자인증
     * */
    @Transactional
    public Long sendSmsForSignup(PhoneAuthDto dto){
        if(checkExistByPhoneNum(dto.getAreaCode(),dto.getLocalNumber())==true){
            throw new RuntimeException("이미 가입한 휴대폰 번호입니다");
        }
        else{
            return requestSmsAuth(dto);
        }
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
    public Long  requestSmsAuth(PhoneAuthDto dto){
        String areaCode = dto.getAreaCode();
        String localNumber = dto.getLocalNumber();
        Long phoneAuthId=null;

        /*랜덤으로 인증번호 생성.*/
        String randomNum=null;
        randomNum = String.format("%06d",((int)(Math.random()*1000000)));

        /*인증번호와 폰번호 db에 저장. */
        PhoneAuth phoneAuth = PhoneAuth.builder()
                .phoneNumber(new PhoneNumber(areaCode,localNumber))
                .certifyNum(randomNum)
                .isCertified(false)
                .build();
        phoneAuthId = phoneAuthRepository.save(phoneAuth).getId();

        /* 문자 전송. */
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        TblSubmitQueue tblSubmitQueue = TblSubmitQueue.builder()
                .usrId("6328")
                .smsGb("1")
                .usedCd("00")
                .reservedFg("I")
                .reservedDttm(time)
                .savedFg("0")
                .rcvPhnId(dto.getAreaCode()+dto.getLocalNumber())
                .sndPhnId("0612778002")
                .sndMsg("인증번호는 ["+randomNum+"]입니다.")
                .contentCnt(0)
                .smsStatus("0")
                .build();

        tblSubmitQueueRepository.save(tblSubmitQueue);
//        tblSubmitQueueRepository.sendSms( time, "01073693401", "인증번호는 ["+randomNum+"]입니다.");

        /*문자전송 제대로 되었는지 확인????? !!!!!일단은 바로 반환하는걸로. dbAgent쪽에 문제발생시 너무 오랫동안 대기하는 이슈가 생길수도
         * 있으므로 일단 바로 api를종료해주고 인증문자를 못받았으면 프론트쪽에서 사용자가 재전송버튼을 누르던지 하는 방식으로. */

        return phoneAuthId;
    }

    /*문자인증 확인 메소드
     * - 넘어온 폰번호와 인증번호를 가지고 테이블상의 값과 일치하는지 확인.
     * - 일치하면 true,아니면 false
     * - 반환 ) 인증번호가 맞으면 true, 틀리면 false. */
    @Transactional
    public boolean checkSmsAuth(Long phoneAuthId, String authNum) throws ResourceNotFoundException {
        /*폰번호에 해당하는 인증번호 가져옴*/
        PhoneAuth phoneAuth = phoneAuthRepository.findById(phoneAuthId)
                .orElseThrow(()->new ResourceNotFoundException("phoneAuth not found for this id :: "+phoneAuthId));

        if(phoneAuth.getCertifyNum().equals(authNum)){
            phoneAuth.setIsCertified(true);
//            phoneAuthRepository.delete(phoneAuth);
            return true;
        }
        else return  false;
    }

    /*uid중복 확인 메소드*/
    @Transactional
    public int checkUidDup(String uid){
        if(Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,6}$",uid)){
            return 2;
        }
        else{
            if(memberRepository.existsByUid(uid)){return 1;}
            else return 0;
        }
    }
    /*닉네임 중복 확인 메소드*/
    @Transactional
    public int checkNickNameDup(String nickName){
        if(nickName.length()<4 && nickName.length() > 10){return 2; }
        else{
            if(memberRepository.existsByNickName(nickName)){return 1;}
            else return 0;
        }
    }

    /*회원가입 중간단계 - 회원정보입력*/
    @Transactional
    public Long insertMemberInfo(SignUpDto signUpDto) throws ResourceNotFoundException {
        Member member = null;
        System.out.println("================\n test >>> in insertMemberInfo()  \n================");

        if(signUpDto.getMemberId()!=null /*&& (!signUpDto.getMemberId().equals(""))*/ ) {
            member = memberRepository.findById(signUpDto.getMemberId())
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this id :: " + signUpDto.getMemberId()));
        }

        /*회원가입 단계 재시도 회원인지 판별. isCertified가 false이고, memberId에 해당하는 member와 입력받은 uid가 일치하면 재시도 회원*/
        if(member!=null && member.getIsCertified()==false && member.getUid().equals(signUpDto.getEmail())) {
            return member.getId();
        }

        /*uid 중복 확인*/
        int checkUid = checkUidDup(signUpDto.getEmail());
        if(checkUid==1){            throw new EmailDupException("이메일이 중복됩니다");        }
        else if(checkUid==2){throw new RuntimeException("이메일 형식이 맞지 않습니다.");}
        /*닉네임 중복 확인*/
        int checkNickName = checkNickNameDup(signUpDto.getNickName());
        if(checkNickName==1){ throw new RuntimeException("닉네임이 중복됩니다"); }
        else if(checkNickName==2){throw new RuntimeException("닉네임은 4자 이상 10자 이하이어야 합니다.");}

        /*회원 정보 저장*/
        /*비밀번호 자바 암호화*/
        String encodedPw = encoder.encode(signUpDto.getPw());
        System.out.println("================\n test >>> encodedPw : "+encodedPw+"\n================");

        /*sns를 통해 가입하는경우.*/
        if(member!=null && member.getIsCertified()==false && member.getSnsId()!=null){
            member.setUid(signUpDto.getEmail());
            member.setNickName(signUpDto.getNickName());
            member.setPassword(encodedPw);
            member.setEmail(signUpDto.getEmail());

            member = memberRepository.save(member);
        }
        /*이메일을 통해 가입하는 경우.*/
        else{
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            member = Member.builder()
                    .uid(signUpDto.getEmail())
                    .memberName(null)//임시값. 수정필요.
                    .nickName(signUpDto.getNickName())
                    .password(encodedPw)
                    .email(signUpDto.getEmail())
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(null)
                    .snsId(null)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            member = memberRepository.save(member);
        }
        System.out.println("================\n test >>> memberId : "+member.getId()+"\n================");
        return member.getId();
    }

    /*nice 성공*/
    @Transactional
    public String niceSuccess(String inputMemberId, String certifiedNo, String name, String phnum, String inputGender) throws ResourceNotFoundException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        System.out.println("================\n test >>> in niceSuccess() \n================");
        System.out.println("================\n test >>> memberId : "+inputMemberId+"\n================");
        String encodedSessionToken=null;
        Long memberId =  Long.parseLong(inputMemberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+memberId));

        String areaCode = phnum.substring(0,3);
        String localNumber = phnum.substring(3);
        int checkPhnumDup = memberRepository.existsByAreaCodeAndLocalNumber(areaCode,localNumber);
        if(checkPhnumDup>0){return null;}

        Integer genderInt = Integer.parseInt(inputGender);
        Gender gender = (genderInt == 0)? Gender.girl : Gender.boy;

        member.setMemberName(name);
        member.setPhoneNumber(new PhoneNumber(areaCode,localNumber));
        member.setGender(gender);
        member.setIsCertified(true);
        member.setCertifiedNo(certifiedNo);
        member.setIsActive(true);
        memberRepository.save(member);

        /*세션토큰생성.*/
        String rawToken = member.getUid() + LocalDateTime.now();
        String sessionToken = encoder.encode(rawToken);

        encodedSessionToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
        member.setSessionToken(sessionToken);
        return encodedSessionToken;
    }
    /*nice 실패*/
    @Transactional
    public void niceFail(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+memberId));
        memberRepository.delete(member);
    }

    public String requestReplace (String paramValue, String gubun) {

        String result = "";

        if (paramValue != null) {

            paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            paramValue = paramValue.replaceAll("\\*", "");
            paramValue = paramValue.replaceAll("\\?", "");
            paramValue = paramValue.replaceAll("\\[", "");
            paramValue = paramValue.replaceAll("\\{", "");
            paramValue = paramValue.replaceAll("\\(", "");
            paramValue = paramValue.replaceAll("\\)", "");
            paramValue = paramValue.replaceAll("\\^", "");
            paramValue = paramValue.replaceAll("\\$", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("@", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll(";", "");
            paramValue = paramValue.replaceAll(":", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("--", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll(",", "");

            if(gubun != "encodeData"){
                paramValue = paramValue.replaceAll("\\+", "");
                paramValue = paramValue.replaceAll("/", "");
                paramValue = paramValue.replaceAll("=", "");
            }

            result = paramValue;

        }
        return result;
    }

    /*비번 변경을 위한 문자인증 요청 메소드*/
    @Transactional
    public Long sendSmsForPwReset(PhoneAuthDto dto){
        /*해당 번호로 가입되어있는 회원이 있는지 확인*/
        if(checkExistByPhoneNum(dto.getAreaCode(),dto.getLocalNumber())==false){
            throw new RuntimeException("가입되지 않은 휴대폰 번호입니다");
        }
        /*문자인증 요청*/
        else{
            return requestSmsAuth(dto);
        }
    }
    /*아이디확인 및 비밀번호 재설정 - 인증번호 통과후 이름과 아이디 확인. */
    @Transactional
    public CheckNameAndUidDto getNameAndUid(CheckUidDto dto) throws ResourceNotFoundException {
        PhoneAuth phoneAuth = phoneAuthRepository.findById(dto.getPhoneAuthId())
                .orElseThrow(()->new ResourceNotFoundException("phoneAuth not found for this id :: "+dto.getPhoneAuthId()));

        if(phoneAuth.getIsCertified()==false){
            throw new RuntimeException("인증이 확인되지 않은 번호입니다. ");
        }

        Member member = memberRepository.findByAreaCodeAndLocalNumber(phoneAuth.getPhoneNumber().getAreaCode(),phoneAuth.getPhoneNumber().getLocalNumber());
        if(member == null){
            throw new RuntimeException("해당 전화번호로 가입한 회원이 없습니다.");
        }

        CheckNameAndUidDto resultDto = CheckNameAndUidDto.builder()
                .memberName(member.getMemberName())
                .uid(member.getUid())
                .build();
        return resultDto;
    }
    /*비번변경 메소드
     * - 비번암호화하여 번호에 해당하는 멤버의 비번필드에 update. */
    @Transactional
    public boolean updatePw(ResetPwDto resetPwDto) throws ResourceNotFoundException {
        PhoneAuth phoneAuth = phoneAuthRepository.findById(resetPwDto.getPhoneAuthId())
                .orElseThrow(()->new ResourceNotFoundException("phoneAuth not found for this id :: "+resetPwDto.getPhoneAuthId()));

        if(phoneAuth.getIsCertified()==false){
            throw new RuntimeException("인증이 확인되지 않은 번호입니다. ");
        }
        /*비번 암호화*/
        String encodedPw = encoder.encode(resetPwDto.getNewPw());

        /*세션토큰에 해당하는 멤버의 비번필드 업데이트*/
        Member member = memberRepository.findByAreaCodeAndLocalNumber(
                phoneAuth.getPhoneNumber().getAreaCode(),phoneAuth.getPhoneNumber().getLocalNumber()
        );

        member.setPassword(encodedPw);

        /*문자인증테이블 phoneAuth에서 데이터 삭제*/
        phoneAuthRepository.delete(phoneAuth);

        return true;
    }

    /*id찾기를 위한 문자인증 메소드*/
    @Transactional
    public String findId(Long phoneAuthId, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        String uid = member.getUid();

        String[] tempUid = uid.split("@");
        tempUid[0] = tempUid[0].substring(0,tempUid[0].length()-2)+"**";
        tempUid[1] = "**"+tempUid[1].substring(2);

        return tempUid[0]+tempUid[1];
    }


    /*pass인증*/
//    @Transactional
//    public String passAuth(String code, String state, String error, String message) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, ResourceNotFoundException {
//        PassAuthResponseDto resultDto=new PassAuthResponseDto();
//        String passClientId = "uWHHuitm5at159jXPlc5";
//        String passClientPw = "c30dbcd9c06a9b0211e12327549537fd6169876092f9f693bce172664690a909";
//
//        Member member = memberRepository.findById(Long.parseLong(state))
//                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+state));
//
//        /*받은 응답이 에러가있을경우 예외처리.*/
//        if(error!=null){
//            throw new RuntimeException(error + ", "+message);
//        }
//
//        /*접근코드 받아오기. */
//        String url = "https://id.passlogin.com/oauth2/token";
//        String method = "POST";
//        Map<String,String> parameter = new HashMap<String, String>();
//        parameter.put("grant_type","authorization_code");
//        parameter.put("client_id",passClientId);
//        parameter.put("client_secret",passClientPw);
//        parameter.put("code",code);
//        parameter.put("state","sample");
////        parameter.put("refresh_token",""); 갱신때 필수.
////        parameter.put("access_token","");     삭제때 필수.
////        parameter.put("service_provider",""); 삭제때 필수.
//
//        String responseForAccessCode = sendRequest(url,method,parameter,"");
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String,Object> mapForAccessCode = mapper.readValue(responseForAccessCode, Map.class);
//
//        String accessToken = (String)mapForAccessCode.get("access_token");
////        String tokenType = (String)mapForAccessCode.get("token_type");
////        Integer expiresIn = (Integer)mapForAccessCode.get("expires_in");
////        String stateForAccessToken = (String)mapForAccessCode.get("state");
//        String errorForAccessToken = (String)mapForAccessCode.get("error");
//        String messageForAccessToken = (String)mapForAccessCode.get("message");
//
//        if(errorForAccessToken!=null){
//            throw new RuntimeException("pass 접근코드 받기 에러\nerror : "+errorForAccessToken+"\nmessage : "+messageForAccessToken);
//        }
//
//        /*회원정보 받아오기. */
//        url = "https://id.passlogin.com/v1/user/me";
//        method = "GET";
//
//        String responseForUsrInfo = sendRequest(url,method,new HashMap<String,String>(),"Bearer "+accessToken);
//        Map<String,Object> mapForUsrInfo = mapper.readValue(responseForUsrInfo, Map.class);
//
//        Map<String,Object> userInfo = (Map<String,Object>)mapForUsrInfo.get("user");
//
//        String plid = (String)userInfo.get("plid");
//        String phoneNo = (String)userInfo.get("phoneNo");
//        String name = (String)userInfo.get("name");
//
//        if(memberRepository.existsByCertifiedNo(plid)){
//            throw new RuntimeException("이미 가입된 회원입니다.");
//        }
//
//        phoneNo = AES.aesDecode(phoneNo, passClientPw.substring(0,16));
//        name = AES.aesDecode(name, passClientPw.substring(0,16));
//
//        /*state로 받은 member의 정보에 pass에서 가져온값 저장*/
//        member.setMemberName(name);
//        member.setCertifiedNo(plid);
//        member.setIsCertified(true);
//        member.setPhoneNumber(new PhoneNumber(phoneNo.substring(0,3),phoneNo.substring(3)));
//        memberRepository.save(member);
//
//        String sessionToken=null;
//        /*세션토큰 생성 및 저장. */
//        String rawToken = member.getUid() + LocalDateTime.now();
//        sessionToken = encoder.encode(rawToken);
//
//        String encodingToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
//        member.setSessionToken(sessionToken);
//        return encodingToken;
//    }

    /*sns로그인. kakao*/
    @Transactional
    public SnsLoginResponseDto snsLoginForKakao(String code, String state, String error) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SnsLoginResponseDto resultDto=new SnsLoginResponseDto();
        resultDto.setSnsType("kakao");
        String clientId = "f0685b27f74d3f456d396195ca40796e";
        String redirectUrl = "https://www.fishkingapp.com/v2/api/kakaoAuthCode";
//        String clientSecret = "LhhI6bSQYOCzBf7FLfnLGA0Ud2qsGTkV";

        /*받은 응답이 에러가있을경우 예외처리.*/
        if(error!=null){
            throw new RuntimeException("인증 코드 요청 에러\nerror code : "+ error );
        }

        /*접근코드 받아오기. */
        System.out.println("get accessToken");
        String url = "https://kauth.kakao.com/oauth/token";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();
        parameter.put("grant_type","authorization_code");
        parameter.put("client_id",clientId);
        parameter.put("redirect_uri",redirectUrl);
//        parameter.put("client_secret",clientSecret);
        parameter.put("code",code);
//        parameter.put("refresh_token",""); 갱신때 필수.
//        parameter.put("access_token","");     삭제때 필수.
//        parameter.put("service_provider",""); 삭제때 필수.

        String responseForAccessCode = sendRequest(url,method,parameter,"");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> mapForAccessCode = mapper.readValue(responseForAccessCode, Map.class);

//        String responseError = (String)mapForAccessCode.get("error");//카카오엔 따로 없음.
//        String responseErrorDescription = (String)mapForAccessCode.get("error_description");//카카오엔 따로 없음.
        String accessToken = (String)mapForAccessCode.get("access_token");
//        String refreshToken = (String)mapForAccessCode.get("refreshToken");
        String tokenType = (String)mapForAccessCode.get("token_type");
//        String expiresInString = (String)mapForAccessCode.get("expires_in");
//        Integer expiresIn = null;
//        if(expiresInString!=null){expiresIn = Integer.parseInt(expiresInString);}

//        if(responseError!=null){
//            throw new RuntimeException("접근 토큰 요청 에러\nerror code : "+ responseError + "\nerror description : "+responseErrorDescription);
//        }
        System.out.println("accessToken :: "+accessToken);

        /*회원정보 받아오기. */
        System.out.println("회원정보 받아오기");
        url = "https://kapi.kakao.com/v2/user/me";
        method = "POST";

        String responseForUsrInfo = sendRequest(url,method,new HashMap<String,String>(),"Bearer "+accessToken);
        Map<String,Object> mapForUsrInfo = mapper.readValue(responseForUsrInfo, Map.class);
//        Map<String,Object> usrInfo = (Map<String,Object>)mapForUsrInfo.get("id");

        String errorCode = (String)mapForUsrInfo.get("code");
        String errorMessage = (String)mapForUsrInfo.get("msg");
        Long usrId = new Long((Integer)mapForUsrInfo.get("id"));
//        String usrNickName = (String)usrInfo.get("nickname");
//        String usrName = (String)usrInfo.get("name");
//        String usrEmail = (String)usrInfo.get("email");
//        String usrGender = (String)usrInfo.get("gender");
//        String usrAge = (String)usrInfo.get("age");
//        String usrProfileImage = (String)usrInfo.get("profile_image");
//        String usrMobile = (String)usrInfo.get("mobile");

        /*에러응답시 예외처리*/
        if(errorCode!=null){
            throw new RuntimeException("프로필 조회 에러\ncode : "+errorCode+"\nmsg : "+errorMessage);
        }

        /*이미 가입된 회원이 존재하면 로그인처리, 아니면 회원가입처리. */
        Member member = memberRepository.findBySnsIdAndSnsType(usrId.toString(), SNSType.kakao);
        /*이미 가입된 회원일 경우, 로그인 처리. */
        if(member !=null && member.getIsCertified()==true){
            resultDto.setResultType("login");

            /*세션토큰이 이미 존재하면 해당세션토큰반환*/
            if(member.getSessionToken()!=null){
                String encodingToken = AES.aesEncode(member.getSessionToken(),env.getProperty("encrypKey.key"));
                resultDto.setSessionToken(encodingToken);
            }
            /*세션토큰이 존재하지 않으면 새로생성한 세션토큰반환.*/
            else{
                String rawToken = member.getUid() + LocalDateTime.now();
                String sessionToken = encoder.encode(rawToken);

                String encodingToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
                member.setSessionToken(sessionToken);
                resultDto.setSessionToken(encodingToken);
            }
            return resultDto;
        }
        /*회원가입 중간에 나갔었던 경우. */
        else if(member !=null && member.getIsCertified()==false){
            memberRepository.deleteById(member.getId());

            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
                    .password("tempPassword")//임시값. 수정필요.
                    .email("tempEmail")//임시값. 수정필요.
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.kakao)
                    .snsId(usrId.toString())
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
        /*처음 sns로 로그인한 회원인 경우, 회원가입처리.
         * - 회원가입페이지로 라다이렉트.
         * - sns api에서 넘겨준 sns연동id를 반환. */
        else{
            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password("tempPassword")//임시값. 수정필요.
                    .email("tempEmail")//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.kakao)
                    .snsId(usrId.toString())
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
    }

    /*sns로그인. 페북*/
    @Transactional
    public SnsLoginResponseDto snsLoginForFacebook(String code) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SnsLoginResponseDto resultDto=new SnsLoginResponseDto();
        resultDto.setSnsType("facebook");
        String clientId = "697267061151978";
        String redirectUrl = "https://www.fishkingapp.com/v2/api/facebookAuthCode";
        String clientSecret = "9fd9e2b178cedaf3f3af5f3e090f03d1";

        /*받은 응답이 에러가있을경우 예외처리.*/
//        if(error!=null){
//            throw new RuntimeException("인증 코드 요청 에러\nerror code : "+ error );
//        }

        /*접근코드 받아오기. */
        System.out.println("get accessToken");
        String url = "https://graph.facebook.com/v9.0/oauth/access_token?" +
                "client_id="+clientId+
                "&redirect_uri="+redirectUrl+
                "&client_secret="+clientSecret+
                "&code="+code;
        String method = "GET";

        String responseForAccessCode = sendRequest(url,method,new HashMap<String,String>(),"");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> mapForAccessCode = mapper.readValue(responseForAccessCode, Map.class);

//        String responseError = (String)mapForAccessCode.get("error");//카카오엔 따로 없음.
//        String responseErrorDescription = (String)mapForAccessCode.get("error_description");//카카오엔 따로 없음.
        String accessToken = (String)mapForAccessCode.get("access_token");
//        String refreshToken = (String)mapForAccessCode.get("refreshToken");
        String tokenType = (String)mapForAccessCode.get("token_type");
//        String expiresInString = (String)mapForAccessCode.get("expires_in");
//        Integer expiresIn = null;
//        if(expiresInString!=null){expiresIn = Integer.parseInt(expiresInString);}

//        if(responseError!=null){
//            throw new RuntimeException("접근 토큰 요청 에러\nerror code : "+ responseError + "\nerror description : "+responseErrorDescription);
//        }
        System.out.println("accessToken :: "+accessToken);

        /*회원정보 받아오기. */
        System.out.println("회원정보 받아오기");
        url = "https://graph.facebook.com/me?fields=id&access_token="+accessToken;
        method = "GET";

        String responseForUsrInfo = sendRequest(url,method,new HashMap<String,String>(),"");
        Map<String,Object> mapForUsrInfo = mapper.readValue(responseForUsrInfo, Map.class);
//        Map<String,Object> usrInfo = (Map<String,Object>)mapForUsrInfo.get("id");

//        String errorCode = (String)mapForUsrInfo.get("code");
//        String errorMessage = (String)mapForUsrInfo.get("msg");
        String usrId = (String)mapForUsrInfo.get("id");
//        String usrNickName = (String)usrInfo.get("nickname");
//        String usrName = (String)usrInfo.get("name");
//        String usrEmail = (String)usrInfo.get("email");
//        String usrGender = (String)usrInfo.get("gender");
//        String usrAge = (String)usrInfo.get("age");
//        String usrProfileImage = (String)usrInfo.get("profile_image");
//        String usrMobile = (String)usrInfo.get("mobile");

        /*에러응답시 예외처리*/
//        if(errorCode!=null){
//            throw new RuntimeException("프로필 조회 에러\ncode : "+errorCode+"\nmsg : "+errorMessage);
//        }

        /*이미 가입된 회원이 존재하면 로그인처리, 아니면 회원가입처리. */
        Member member = memberRepository.findBySnsIdAndSnsType(usrId, SNSType.facebook);
        /*이미 가입된 회원일 경우, 로그인 처리. */
        if(member !=null && member.getIsCertified() == true){
            resultDto.setResultType("login");

            /*세션토큰이 이미 존재하면 해당세션토큰반환*/
            if(member.getSessionToken()!=null){
                String encodingToken = AES.aesEncode(member.getSessionToken(),env.getProperty("encrypKey.key"));
                resultDto.setSessionToken(encodingToken);
            }
            /*세션토큰이 존재하지 않으면 새로생성한 세션토큰반환.*/
            else{
                String rawToken = member.getUid() + LocalDateTime.now();
                String sessionToken = encoder.encode(rawToken);

                String encodingToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
                member.setSessionToken(sessionToken);
                resultDto.setSessionToken(encodingToken);
            }
            return resultDto;
        }
        else if(member != null && member.getIsCertified() == false){
            memberRepository.delete(member);

            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password("tempPassword")//임시값. 수정필요.
                    .email("tempEmail")//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.facebook)
                    .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
        /*처음 sns로 로그인한 회원인 경우, 회원가입처리.
         * - 회원가입페이지로 라다이렉트.
         * - sns api에서 넘겨준 sns연동id를 반환. */
        else{
            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password("tempPassword")//임시값. 수정필요.
                    .email("tempEmail")//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.facebook)
                    .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
    }

    /*sns로그인. naver
     * - OAuth방식으로 인증코드를 받아 접근코드받기, 회원정보가져오기를 수행하고 처음 접속하는 회원이면 회원가입처리, 아니면 로그인 처리를 해준다.
     * - */
    @Transactional
    public SnsLoginResponseDto snsLoginForNaver(String code, String state, String error, String errorDescription) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SnsLoginResponseDto resultDto=new SnsLoginResponseDto();
        resultDto.setSnsType("naver");
        String clientId = "xQF6XDWPhMC665JO2kSq";
        String clientSecret = "shKqzGtgR1";

        /*받은 응답이 에러가있을경우 예외처리.*/
        if(error!=null){
            throw new RuntimeException("인증 코드 요청 에러\nerror code : "+ error + "\nerror description : "+errorDescription);
        }

        /*접근코드 받아오기. */
        String url = "https://nid.naver.com/oauth2.0/token";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();
        parameter.put("grant_type","authorization_code");
        parameter.put("client_id",clientId);
        parameter.put("client_secret",clientSecret);
        parameter.put("code",code);
        parameter.put("state","sample");
//        parameter.put("refresh_token",""); 갱신때 필수.
//        parameter.put("access_token","");     삭제때 필수.
//        parameter.put("service_provider",""); 삭제때 필수.

        String responseForAccessCode = sendRequest(url,method,parameter,"");
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> mapForAccessCode = mapper.readValue(responseForAccessCode, Map.class);

        String responseError = (String)mapForAccessCode.get("error");
        String responseErrorDescription = (String)mapForAccessCode.get("error_description");
        String accessToken = (String)mapForAccessCode.get("access_token");
//        String refreshToken = (String)mapForAccessCode.get("refreshToken");
        String tokenType = (String)mapForAccessCode.get("token_type");
//        String expiresInString = (String)mapForAccessCode.get("expires_in");
//        Integer expiresIn = null;
//        if(expiresInString!=null){expiresIn = Integer.parseInt(expiresInString);}

        if(responseError!=null){
            throw new RuntimeException("접근 토큰 요청 에러\nerror code : "+ responseError + "\nerror description : "+responseErrorDescription);
        }

        /*회원정보 받아오기. */
        url = "https://openapi.naver.com/v1/nid/me";
        method = "POST";

        String responseForUsrInfo = sendRequest(url,method,new HashMap<String,String>(),tokenType+" "+accessToken);
        Map<String,Object> mapForUsrInfo = mapper.readValue(responseForUsrInfo, Map.class);
        Map<String,Object> usrInfo = (Map<String,Object>)mapForUsrInfo.get("response");

        String resultCode = (String)mapForUsrInfo.get("resultcode");
        String message = (String)mapForUsrInfo.get("message");
        String usrId = (String)usrInfo.get("id");
//        String usrNickName = (String)usrInfo.get("nickname");
//        String usrName = (String)usrInfo.get("name");
//        String usrEmail = (String)usrInfo.get("email");
//        String usrGender = (String)usrInfo.get("gender");
//        String usrAge = (String)usrInfo.get("age");
//        String usrProfileImage = (String)usrInfo.get("profile_image");
//        String usrMobile = (String)usrInfo.get("mobile");

        /*에러응답시 예외처리*/
        if(!resultCode.equals("00")){
            throw new RuntimeException("프로필 조회 에러\nresultcode : "+resultCode+"\nmessage : "+message);
        }

        /*이미 가입된 회원이 존재하면 로그인처리, 아니면 회원가입처리. */
        Member member = memberRepository.findBySnsIdAndSnsType(usrId, SNSType.naver);
        /*이미 가입된 회원일 경우, 로그인 처리. */
        if(member !=null && member.getIsCertified() == true){
            resultDto.setResultType("login");

            /*세션토큰이 이미 존재하면 해당세션토큰반환*/
            if(member.getSessionToken()!=null){
                String encodingToken = AES.aesEncode(member.getSessionToken(),env.getProperty("encrypKey.key"));
                resultDto.setSessionToken(encodingToken);
            }
            /*세션토큰이 존재하지 않으면 새로생성한 세션토큰반환.*/
            else{
                String rawToken = member.getUid() + LocalDateTime.now();
                String sessionToken = encoder.encode(rawToken);

                String encodingToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
                member.setSessionToken(sessionToken);
                resultDto.setSessionToken(encodingToken);
            }
            return resultDto;
        }
        else if(member != null && member.getIsCertified() == false){
            memberRepository.delete(member);

            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password(usrId)//임시값. 수정필요.
                    .email(usrId)//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.naver)
                    .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
        /*처음 sns로 로그인한 회원인 경우, 회원가입처리.
         * - 회원가입페이지로 라다이렉트.
         * - sns api에서 넘겨준 sns연동id를 반환. */
        else{
            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password(usrId)//임시값. 수정필요.
                    .email(usrId)//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.naver)
                    .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
            /*String[] phoneNumber = convertPhoneNumber(usrMobile);
            if(memberRepository.existsByAreaCodeAndLocalNumber(phoneNumber[0], phoneNumber[1])>0){
                throw new RuntimeException("이미 가입한 전화번호입니다.");
            }

            *//*저장할 회원정보 변환*//*
             *//*Gender gender=null;
            if(usrGender.equals("F")){gender=Gender.girl;}
            else if(usrGender.equals("M")){gender=Gender.boy;}
            else if(usrGender.equals("U")){gender=null;}*//*

             *//*기본 프로필 이미지url들 가져옴*//*
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(SNSType.naver.getValue()+usrId)
                    .memberName(usrName)
//                    .nickName(usrNickName)
                    .email(usrEmail)
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(true)
                    .isCertified(true)
                    .snsType(SNSType.naver)
                    .snsId(usrId)
                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            String rawToken = newMember.getUid() + LocalDateTime.now();
            String sessionToken = encoder.encode(rawToken);

            newMember.setSessionToken(sessionToken);
            return sessionToken;*/
        }

    }

    /*apple 로그인*/
    @Transactional
    public SnsLoginResponseDto snsLoginForApple(String idToken) throws ParseException, JsonProcessingException, NoSuchPaddingException,
            InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        System.out.println("idToken : "+idToken);
        SnsLoginResponseDto resultDto = new SnsLoginResponseDto();
        resultDto.setSnsType("apple");

        SignedJWT signedJWT = SignedJWT.parse(idToken);
        ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
        ObjectMapper objectMapper = new ObjectMapper();
        AppleLoginPayLoadDto payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), AppleLoginPayLoadDto.class);
        System.out.println("payload : "+payload.toString());
        String usrId = payload.getSub();
        System.out.println("sub : "+usrId);

        /*이미 가입된 회원이 존재하면 로그인처리, 아니면 회원가입처리. */
        Member member = memberRepository.findBySnsIdAndSnsType(usrId, SNSType.apple);
        /*이미 가입된 회원일 경우, 로그인 처리. */
        if(member !=null && member.getIsCertified() == true){
            resultDto.setResultType("login");

            /*세션토큰이 이미 존재하면 해당세션토큰반환*/
            if(member.getSessionToken()!=null){
                String encodingToken = AES.aesEncode(member.getSessionToken(),env.getProperty("encrypKey.key"));
                resultDto.setSessionToken(encodingToken);
            }
            /*세션토큰이 존재하지 않으면 새로생성한 세션토큰반환.*/
            else{
                String rawToken = member.getUid() + LocalDateTime.now();
                String sessionToken = encoder.encode(rawToken);

                String encodingToken = AES.aesEncode(sessionToken,env.getProperty("encrypKey.key"));
                member.setSessionToken(sessionToken);
                resultDto.setSessionToken(encodingToken);
            }
            return resultDto;
        }
        else if(member != null && member.getIsCertified() == false){
            memberRepository.delete(member);

            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                    .password(usrId)//임시값. 수정필요.
                    .email(usrId)//임시값. 수정필요.
//                    .gender(gender)
                    .roles(Role.member)
                    .profileImage(noProfileImage.getExtraValue1())
                    .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                    .isActive(false)
                    .isCertified(false)
                    .snsType(SNSType.apple)
                    .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                    .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
        /*처음 sns로 로그인한 회원인 경우, 회원가입처리.
         * - 회원가입페이지로 라다이렉트.
         * - sns api에서 넘겨준 sns연동id를 반환. */
        else{
            resultDto.setResultType("signUp");

            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            Member newMember = Member.builder()
                    .uid(LocalDateTime.now().toString() + (int)Math.random()*1000)//임시값. 수정필요.
                    .memberName(null)//임시값. 수정필요.
//                    .nickName(usrNickName)
                .password(usrId)//임시값. 수정필요.
                .email(usrId)//임시값. 수정필요.
//                    .gender(gender)
                .roles(Role.member)
                .profileImage(noProfileImage.getExtraValue1())
                .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                .isActive(false)
                .isCertified(false)
                .snsType(SNSType.apple)
                .snsId(usrId)
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                .build();
            newMember = memberRepository.save(newMember);

            resultDto.setMemberId(newMember.getId());
            return resultDto;
        }
    }

    /*000-0000-0000형식의 전화번호를 앞세자리와 나머지번호부분으로 나누어 반환하는 메소드. */
    String[] convertPhoneNumber(String phoneNumber){
        String[] number = phoneNumber.split("-");
        String[] result=new String[2];

        result[0]=number[0];
        result[1]=number[1]+number[2];

        return result;
    }

    /*요청보내는 메소드*/
    public String sendRequest(String inputUrl, String method, Map<String,String> parameter, String token) throws IOException {
        URL url = new URL(inputUrl);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;

        if(method.equals("GET")){
//            StringJoiner sj = new StringJoiner("&");
//            for(Map.Entry<String,String> entry : parameter.entrySet())
//                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
//                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
//            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
//            int length = out.length;

//            http.setFixedLengthStreamingMode(length);
//            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.setRequestMethod("GET");
            http.setRequestProperty("Authorization",token);
            http.connect();
            System.out.println("http response : "+http);
//            try(OutputStream os = http.getOutputStream()) {
//                os.write(out);
//            }
            // Do something with http.getInputStream()
            int responseCode = http.getResponseCode();
            BufferedReader br;
            System.out.println("response code : "+responseCode);

            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            return res.toString();
        }
        else if(method.equals("POST")){
            /*Map<String,String> arguments = new HashMap<>();
        arguments.put("username", "root");
        arguments.put("password", "sjh76HSn!"); // This is a fake password obviously*/
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : parameter.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.setRequestProperty("Authorization",token);
            http.connect();
            System.out.println("http response : "+http);
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
            // Do something with http.getInputStream()
            int responseCode = http.getResponseCode();
            BufferedReader br;
            System.out.println("response code : "+responseCode);

            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            return res.toString();
        }
        else if(method.equals("JSON")){
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            String json = parameter.get("json");

            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.setRequestProperty("Accept","application/json");
            http.setRequestProperty("Authorization",token);
            http.connect();
            System.out.println("http response : "+http);
            try(OutputStream os = http.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            StringBuilder response = null;
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(http.getInputStream(),"utf-8"))){
                response = new StringBuilder();
                String responseLine = null;
                while((responseLine = br.readLine()) != null){
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());

            }
            return response.toString();
            // Do something with http.getInputStream()
            /*int responseCode = http.getResponseCode();
            BufferedReader br;
            System.out.println("response code : "+responseCode);

            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            }
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();*/

        }
        else{
            return "일치하는 method가 없습니다.";
        }

        /*if(responseCode==200) {
            return res.toString();
        } else {
            return null;
        }*/
    }

    /*로그인
     * - 아디, 비번확인하고 아니면 예외처리??, 맞으면 세션토큰생성하여 저장하고 반환. */
    @Transactional
    public String login(LoginDTO loginDTO) throws ResourceNotFoundException {
        System.out.println("memberId : "+loginDTO.getMemberId()+", pw : "+loginDTO.getPassword()+", token : "+loginDTO.getRegistrationToken());
        String sessionToken=null;
        /*아디,비번 확인*/
        Member member = memberRepository.findByUid(loginDTO.getMemberId())
                .orElseThrow(()->new ResourceNotFoundException("아이디가 존재하지 않습니다"));
        System.out.println("memberId : "+member.getId());
        if(member==null){
            throw new IncorrectIdException("아이디가 존재하지 않습니다");
        }
        else if(encoder.matches(loginDTO.getPassword(),member.getPassword())){//로그인 성공
            System.out.println("login success");
            /*탈퇴한 회원인 경우*/
            if(member.getIsActive() == false){throw new RuntimeException("회원정보가 존재하지 않습니다.");}
            /*세션토큰이 이미존재한다면. 즉, 이미 로그인되어있는 회원이라면 기존의 세션토큰을 반환해줌. */
            if(member.getSessionToken()!=null){
                System.out.println("token already exists");
                member.setRegistrationToken(loginDTO.getRegistrationToken());
                sessionToken = member.getSessionToken();
            }
            else {
                System.out.println("token not exists");
                /*세션토큰 생성 및 저장. */
                String rawToken = member.getUid() + LocalDateTime.now();
                sessionToken = encoder.encode(rawToken);

                System.out.println("token : "+sessionToken);
                member.setRegistrationToken(loginDTO.getRegistrationToken());
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
        Member profileMember = memberRepository.findById(profileUserId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+profileUserId));
        Long meId = null;
        if(sessionToken !=null) {
            Member me = memberRepository.findBySessionToken(sessionToken)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this sessionToken :: " + sessionToken));
            meId = me.getId();
        }
        Company profileCompany = companyRepository.findByMember(profileMember);

        /*가져온 데이터들을 UserProfileDTO에 저장. (본인이 아닌 경우, '업체찜수'를 추가해준다)  */
        UserProfileDTO userProfileDTO=null;
        if(profileMember.getIsActive()) {
            /*공통적으로 필요한 프로필정보 dto에 추가. */
            userProfileDTO = UserProfileDTO.builder()
                    .memberId(profileMember.getId())
                    .nickName((profileMember.getNickName() == null) ? ("이름없음") : (profileMember.getNickName()))
                    .profileImage(env.getProperty("file.downloadUrl") + profileMember.getProfileImage())
                    .backgroundImage(env.getProperty("file.downloadUrl") + profileMember.getProfileBackgroundImage())
                    .isMe(false)
                    .isCompany(false)
                    .build();

            /*본인인 경우를 표시. */
            if (profileUserId.equals(meId)) {
                int fishingDiaryCount = fishingDiaryRepository.countByMember(profileMember);
                ;//회원이 작성한 조행기 개수.
                int reviewCount = reviewRepository.countByMember(profileMember);//회원이 작성한 리뷰 개수
                int likeCount = loveToRepository.countByCreatedBy(profileMember);//회원이 좋아요한 개수.
                userProfileDTO.setIsMe(true);
                userProfileDTO.setPostCount(fishingDiaryCount + reviewCount);
                userProfileDTO.setLikeCount(likeCount);
            }
            /*본인이 아닌 다른 일반회원의 프로필일 경우*/
            else if ((profileUserId != meId) && profileCompany == null) {
                int fishingDiaryCount = fishingDiaryRepository.countByMember(profileMember);//회원이 작성한 조행기 개수.
                int reviewCount = reviewRepository.countByMember(profileMember);//회원이 작성한 리뷰 개수
                int likeCount = loveToRepository.countByCreatedBy(profileMember);//회원이 좋아요한 개수.
                int takeCount = takeRepository.countByCreatedBy(profileMember);//회원이 찜한 개수.
                userProfileDTO.setPostCount(fishingDiaryCount + reviewCount);
                userProfileDTO.setLikeCount(likeCount);
                userProfileDTO.setTakeCount(takeCount);
            }
            /*업체회원인 경우*/
            else if ((profileUserId != meId) && profileCompany != null) {
                int fishingDiaryCount = fishingDiaryRepository.countByMember(profileMember);//업체가 작성한 조항일지 개수.
                int likeCount = loveToRepository.countLikeCountForCompanyProfile(profileMember.getId());//조항일지 등에서 받은 좋아요수.
                int takeCount = takeRepository.countForCompanyProfile(profileCompany.getId());//업체가 받은 총 찜수.
                userProfileDTO.setPostCount(fishingDiaryCount);
                userProfileDTO.setTakeCount(takeCount);
                userProfileDTO.setLikeCount(likeCount);
                userProfileDTO.setIsCompany(true);
                userProfileDTO.setCompanyId(profileCompany.getId());
            }
        }
        /*회원이 탈퇴한 경우*/
        else{
            /*sns관련 필드를 저장하여 임시 member 엔터티 생성. */
            CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
            CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noImg");
            CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup,"noBackImg");

            userProfileDTO = UserProfileDTO.builder()
                    .nickName("탈퇴한 회원입니다.")
                    .profileImage(noProfileImage.getExtraValue1())
                    .backgroundImage(noBackgroundImage.getExtraValue1())
                    .postCount(0)
                    .likeCount(0)
                    .isMe(false)
                    .takeCount(0)
                    .isCompany(false)
                    .build();
        }

        return userProfileDTO;
    }

    /*상대방 게시글 보기*/
    @Transactional
    public Page<FishingDiaryDtoForPage> getUserFishingDiary(Long userId, int page, String token) throws ResourceNotFoundException {
        Member user = memberRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+userId));
        Long memberId = null;
        if(token != null) {
            Member member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this sessionToken ::" + token));
            memberId = member.getId();
        }
        if(user.getIsActive() == false){throw new RuntimeException("해당 회원은 탈퇴한 회원입니다.");}

        Pageable pageable = PageRequest.of(page,10);
//        return fishingDiaryRepository.findByMember(user,member,pageable);
        return fishingDiaryRepository.getFishingDiaryListOrderByCreatedDate(
                null, null,null, null, null, user.getId(), memberId, false,
                null, null, pageable
        );
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
                .memberName(member.getMemberName())
                .nickName((member.getNickName()==null)?("없음"):(member.getNickName()))
                .email(member.getEmail())
                .profileImage(env.getProperty("file.downloadUrl")+member.getProfileImage())
                .profileBackgroundImage(env.getProperty("file.downloadUrl") + member.getProfileBackgroundImage())
                .statusMessage((member.getStatusMessage()==null)?("없음"):(member.getStatusMessage()))
                //!!!!!아래 전화번호는 nullable필드이지만 회원가입시 휴대폰인증을 하므로 무조건 있다고 판단.
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .build();

        return profileManageDTO;
    }

    /*프사변경*/
    @Transactional
    public boolean updateProfileImage(ModifyingProfileImgDto dto, String sessionToken) throws Exception {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        FileEntity newProfileImg = fileRepository.findById(dto.getProfileImgFileId())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getProfileImgFileId()));

        /*기존 프사 fileEntity 삭제*/
        FileEntity preFileEntity = fileRepository.findTop1ByPidAndFilePublishAndIsRepresent(member.getId(), FilePublish.profile,true);
        if(preFileEntity!=null){  uploadService.removeFileEntity(preFileEntity.getId());}

        /*업로드 미리보기요으로 미리 저장된 파일 엔터티를 수정.*/
        newProfileImg.saveTemporaryFile(member.getId());
        newProfileImg.setRepresent(true);

        member.setProfileImage("/"+newProfileImg.getFileUrl()+"/"+newProfileImg.getStoredFile());

        /*if(uploadService.checkFileType(file)!=FileType.image){
            throw new Exception();//!!!!!어떤 예외 던져야할지.
        }*/

        /*프사 추가 및 Member의 profileImage 업데이트. */
        /*Map<String,Object> fileInfo = uploadService.initialFile(file, FilePublish.profile, "");

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
        fileRepository.save(fileEntity);*/

        return true;
    }
    /*프사 내리기*/
    @Transactional
    public boolean deleteProfileImage(String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FileEntity file = fileRepository.findTop1ByPidAndFilePublishAndIsRepresent(member.getId(),FilePublish.profile,true);
        CodeGroup codeGroup = codeGroupRepository.findById(92L)
                .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+92));
        CommonCode commonCode = commonCodeRepository.findByCodeGroupAndCode(codeGroup, "noImg");

        uploadService.removeFileEntity(file.getId());
        member.setProfileImage(commonCode.getExtraValue1());
        return true;
    }
    /*프로필 배경 이미지 변경*/
    @Transactional
    public boolean updateProfileBackgroundImage(ModifyingProfileImgDto dto, String sessionToken) throws Exception {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        FileEntity newProfileImg = fileRepository.findById(dto.getProfileImgFileId())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getProfileImgFileId()));

        /*기존 프사 fileEntity 삭제*/
        FileEntity preFileEntity = fileRepository.findTop1ByPidAndFilePublishAndIsRepresent(member.getId(), FilePublish.profile,false);
        if(preFileEntity!=null){  uploadService.removeFileEntity(preFileEntity.getId());}

        /*업로드 미리보기요으로 미리 저장된 파일 엔터티를 수정.*/
        newProfileImg.saveTemporaryFile(member.getId());
        newProfileImg.setRepresent(false);

        member.setProfileBackgroundImage("/"+newProfileImg.getFileUrl()+"/"+newProfileImg.getStoredFile());

        /*if(uploadService.checkFileType(file)!=FileType.image){
            throw new Exception();//!!!!!어떤 예외 던져야할지.
        }*/

        /*프사 추가 및 Member의 profileImage 업데이트. */
        /*Map<String,Object> fileInfo = uploadService.initialFile(file, FilePublish.profile, "");

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
        fileRepository.save(fileEntity);*/

        return true;
    }
    /*프사배경 내리기*/
    @Transactional
    public boolean deleteProfileBackgroundImage(String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FileEntity file = fileRepository.findTop1ByPidAndFilePublishAndIsRepresent(member.getId(),FilePublish.profile,false);
        CodeGroup codeGroup = codeGroupRepository.findById(92L)
                .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+92));
        CommonCode commonCode = commonCodeRepository.findByCodeGroupAndCode(codeGroup, "noBackImg");

        uploadService.removeFileEntity(file.getId());
        member.setProfileBackgroundImage(commonCode.getExtraValue1());
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

    /*휴대폰번호 변경을 위한 문자인증요청 */
    @Transactional
    public Long sendSmsAuthForModifyPhoneNum(PhoneAuthDto dto){
        if(checkExistByPhoneNum(dto.getAreaCode(),dto.getLocalNumber())==true){
            throw new RuntimeException("이미 가입한 휴대폰 번호입니다");
        }
        else{
            return requestSmsAuth(dto);
        }
    }

    /*휴대폰 번호 변경
     * - dto에 들은 phoneAuth가 인증이된건지 확인 후, token에 해당하는 멤버의 번호를 변경해준다.*/
    @Transactional
    public boolean modifyPhoneNumber(ModifyPhoneNumberDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not fuond for this token :: "+token));
        PhoneAuth phoneAuth = phoneAuthRepository.findById(dto.getPhoneAuthId())
                .orElseThrow(()->new ResourceNotFoundException("phoneAuth not found for this id :: "+dto.getPhoneAuthId()));

        if(phoneAuth.getIsCertified()) {
            member.setPhoneNumber(new PhoneNumber(phoneAuth.getPhoneNumber().getAreaCode(), phoneAuth.getPhoneNumber().getLocalNumber()));
            return true;
        }
        else{
            throw new RuntimeException("문자 인증을 받은 번호가 아닙니다.");
        }
    }

    @Transactional
    public Member getMemberById(Long member_id) {
        return memberRepository.getOne(member_id);
    }

}
