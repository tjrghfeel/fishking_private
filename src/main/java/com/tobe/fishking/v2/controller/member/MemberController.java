package com.tobe.fishking.v2.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.auth.*;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.RegEx;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.AttributeConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Api(tags={"프로필"})
@Controller
@RequestMapping("/v2/api")
public class MemberController {

    @Autowired
    MemberService memberService;

    /*회원가입 - 휴대폰인증
     * - 이미 가입된 번호인지 확인
     * - 실제로 전송가능한 번호인지 확인
     * - 인증 문자 전송. */
    /*@ApiOperation(value = "휴대폰 인증")
    @PostMapping("/smsAuthReq")
    public boolean requestSmsAuth(@ModelAttribute("phoneInfo") PhoneAuthDto phoneAuthDto){
        return memberService.smsAuthForSignUp(phoneAuthDto);
    }*/

    /*회원가입시 문자인증 api
     * */
    /*@ApiOperation(value = "회원가입 문자인증 문자 보내기",notes = "" +
            "- 회원가입시, 문자 인증 문자를 보내고 해당 문자인증 건에 대한 id를 반환한다. 인증번호를 보낼때 이 id값을 같이 보내야 한다. " +
            "- 요청 필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 응답 ) 문자 인증건에 대한 id. ")
    @PostMapping("/signUp/sendSms")
    public Long sendSms(@RequestBody PhoneAuthDto dto){
        return memberService.sendSmsForSignup(dto);
    }*/

    /*문자인증 확인
     * - 인증번호 보내면 일치하는지 확인. */
    @ApiOperation(value = "문자인증확인",notes = "" +
            "- 인증번호가 일치하는지 확인.\n" +
            "- 요청 필드 )\n" +
            "   phoneAuthId : 문자인증건에 대한 id\n" +
            "   authNum : 인증번호\n" +
            "- 반환 ) 일치할경우 true, 아닐경우 false. ")
    @PostMapping("/checkSmsAuth")
    @ResponseBody
    public boolean checkSignUpSmsAuth(
            @RequestBody PhoneAuthCheckDto dto
    ) throws ResourceNotFoundException {
        return memberService.checkSmsAuth(dto.getPhoneAuthId(),dto.getAuthNum());
    }

    /*아이디 중복 확인*/
    @ApiOperation(value = "아이디 중복 확인",notes = "" +
            "- 입력한 아이디가 이미 존재하는지 확인\n" +
            "- 입력 필드 )\n" +
            "   uid : 중복확인할 아이디\n" +
            "- 반환 ) \n" +
            "   0 : 중복 안됨\n" +
            "   1 : 중복 \n" +
            "   2 : 이메일 형식에 맞지 않음")
    @GetMapping("/signUp/checkUidDup")
    @ResponseBody
    public int checkUidDup(@RequestParam("uid") String uid){
        return memberService.checkUidDup(uid);
    }

    /*닉네임 중복 확인*/
    @ApiOperation(value = "닉네임 중복 확인", notes = "" +
            "- 입력 필드 ) \n" +
            "   nickName : 중복확인할 닉네임"
    )
    @GetMapping("/signUp/checkNickNameDup")
    @ResponseBody
    public int checkNickNameDup(@RequestParam("nickName") String nickName){
        return memberService.checkNickNameDup(nickName);
    }

    /*회원가입 중간단계 - 회원정보입력*/
    @ApiOperation(value = "회원정보입력 완료 및 pass인증 요청",notes = "" +
            "회원가입 단계중, 회원정보 입력후 pass인증 요청 api. pass인증버튼 클릭시 호출, 이전단계에서 입력한 회원정보를 함께 보내주어야한다. \n" +
            "회원정보 임시저장 및 pass인증을 호출해 준다. \n" +
            "요청 필드 ) \n" +
            "- memberId : Long / 선택 / sns를 통해 회원가입진행중일 경우 입력. \n\tㄴ sns로그인 후 리다이렉트 url에 있는 파라미터 'memberId'를 입력해주면된다.\n" +
            "- email : String / 필수 / 이메일. 이메일 형식에 맞아야한다\n" +
            "- pw : String / 필수 / 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "- nickName : String / 필수 / 닉네임. 4~10자. \n" +
            "" )
    @PostMapping("/signUp/infoAndPassRequest")
    @ResponseBody
    public void insertMemberInfo(@RequestBody SignUpDto dto, HttpServletResponse response) throws ResourceNotFoundException, IOException {
        /*회원정보 저장. */
        Long memberId = memberService.insertMemberInfo(dto);

        /*pass인증 요청.*/
        String url = "https://id.passlogin.com/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=uWHHuitm5at159jXPlc5" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/passAuthCode" +
                "&state="+memberId;
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();

        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");
    }

    /*회원가입 - 회원정보입력
     * - 이메일이 중복되지 않으면 true, 세션에 회원가입정보 저장.
     * - 중복되면 false반환.  */
    /*@ApiOperation(value = "회원가입 - 회원정보입력", notes = "" +
            "- 회원정보와 함께 보낼경우 회원가입 처리해주는 api \n" +
            "- 요청 필드 ) \n" +
            "   email : String / 이메일. 이메일 형식에 맞아야한다\n" +
            "   pw : String / 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "   nickName : String / 닉네임. 4~10자. \n" +
            *//*"   phoneAuthId : Long / 문자인증건의 id.\n" +*//*
            "   snsId : String / 연동된sns id\n" +
            "   snsType : String / 연동된 sns의 종류(아래 값들 중 하나를 입력)\n" +
            "       ㄴ kakao : 카카오\n" +
            "       ㄴ naver : 네이버 \n" +
            "       ㄴ facebook : 페이스북\n" +
            "   memberName : String / 실명\n"+
            "   areaCode : String / 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : String / 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "   passLoginId : String / pass로그인 연동 식별자. \n" +
            "- 반환 ) 회원가입된 회원의 id  ")
    @PostMapping("/signUp/info")
    public Long checkEmailDup(@RequestBody @Valid SignUpDto signUpDto) throws ResourceNotFoundException {
        return memberService.signUp(signUpDto);
    }*/

    /*비밀번호 찾기(재설정) 인증.
     * - 번호누르고 발송버튼 누르면 들어오는 요청. */
    @ApiOperation(value = "비밀번호 찾기(재설정) 또는 아이디찾기를 위한 문자인증",notes = "" +
            "- 비밀번호 재설정시 또는 아이디 찾기를 위해 문자인증요청하는 api. \n" +
            "- 요청필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 응답 ) 문자인증건의 id ")
    @PostMapping("/findPw/smsAuthReq")
    @ResponseBody
    public Long requestSmsAuthForPwSearch(@RequestBody @Valid PhoneAuthDto dto){
        return memberService.sendSmsForPwReset(dto);
    }
    /*아이디확인 및 비밀번호 재설정 - 인증번호 통과후 이름과 아이디 알려주는 api. */
    @ApiOperation(value = "아아디 확인 및 비밀번호 재설정 - 이름, 아이디 확인",notes = "" +
            "요청필드 ) \n" +
            "- phoneAuthId : Long / 문자인증건의 id\n" +
            "응답 필드 ) \n" +
            "- memberName : String / 회원실명\n" +
            "- uid : String / 회원 아이디\n")
    @PutMapping("/findPw/uid")
    @ResponseBody
    public CheckNameAndUidDto getNameAndUid(
            @RequestBody CheckUidDto dto
    ) throws ResourceNotFoundException {
        return memberService.getNameAndUid(dto);
    }
    /*비밀번호 찾기 이후, 재설정.
     * - 핸드폰인증 후 뜨는 비번변경창에서 새 비밀번호입력후 변경클릭하면 들어오는 요청.
     * - 세션토큰에 해당하는 member의 비번을 새 비밀번호로 변경*/
    @ApiOperation(value = "비밀번호 변경",notes = "" +
            "- 비밀번호 재설정에서 문자인증 통과후, 비번을 재설정하는 api.\n" +
            "- 요청 필드 )\n" +
            "   newPw : String / 새 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "   phoneAuthId : 문자인증건의 id")
    @PutMapping("/findPw/updatePw")
    @ResponseBody
    public boolean updatePw(
            @RequestBody @Valid ResetPwDto resetPwDto
    ) throws ResourceNotFoundException {
        return memberService.updatePw(resetPwDto);
    }

    /*아이디 찾기*/
    /*@ApiOperation(value = "아이디 찾기",notes = "" +
            "- 문자인증 이후, 아이디 반환해주는 api. \n ")
    @GetMapping("/findId/smsAuthReq")
    public String findId(@RequestParam Long phoneAuthId, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.findId(phoneAuthId, token);
    }*/

    /*로그인*/
    @ApiOperation(value = "로그인",notes = "" +
            "요청 필드 ) \n" +
            "- memberId : String / 필수 / 회원 아이디\n" +
            "- password : String / 필수 / 비밀번호\n" +
            "- registrationToken : String / 선택 / 푸쉬알림을 위한 기기의 등록 토큰\n")
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody @Valid LoginDTO loginDTO) throws ResourceNotFoundException {
        return memberService.login(loginDTO);
    }

    /*로그아웃*/
    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    @ResponseBody
    public boolean logout(HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.logout(sessionToken);
    }

    /*pass인증 callback url*/
    @ApiOperation(value = "pass인증 callback url",notes = "" +
            "회원가입 단계 마지막 본인인증 단계에서 사용하는 pass의 callback url.\n" +
            "실명과 핸드폰번호 데이터를 가지고 회원가입 처리 후, 로그인 처리.  \n" +
            "")
    @RequestMapping("/passAuthCode")
    @ResponseBody
    public void getPassAuthCode(
            @RequestParam(value = "code",required = false) String code,
            @RequestParam(value = "state",required = false) String state,
            @RequestParam(value = "error",required = false) String error,
            @RequestParam(value = "message",required = false) String message,
            HttpServletResponse response
    ) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ResourceNotFoundException {
        String sessionToken = memberService.passAuth(code,state,error,message);

        /*!!!!!form을 사용해 post로 프런트의 페이지로 보내주는 jsp로 이동. view사용하여 model에 세션코드저장해서보내줌.*/
        response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+sessionToken);

        return ;
    }

    /*kakao 인증코드 받는 메소드. */
    @ApiOperation(value = "kakao 로그인 인증코드 받는 api",notes = "")
    @RequestMapping("/kakaoAuthCode")
    public String getKakaoAuthCode(
            @RequestParam(value = "code",required = false) String code,
            @RequestParam(value = "state",required = false) String state,
            @RequestParam(value = "error",required = false) String error,
            Model model,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SnsLoginResponseDto dto = memberService.snsLoginForKakao(code, state, error);

        if(dto.getResultType().equals("signUp")){
            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
        }
        else{
            model.addAttribute("accessToken",dto.getSessionToken());
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력. ~/member/signup로 보내라고함.
        }
        return "";
    }
    /*페이스북 인증코드 받는 메소드*/
    @ApiOperation(value = "페이스북 인증 코드 받는 api",notes = "" +
            "")
    @GetMapping("/facebookAuthCode")
    public void getFacebookAuthCode(
//            @RequestParam("response_type") String responseType,
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SnsLoginResponseDto dto = memberService.snsLoginForFacebook(code);

        if(dto.getResultType().equals("signUp")){
            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }
    /*페이스북 로그인 연동 id받는 메소드 */
    /*@ApiOperation(value = "페이스북 로그인 연동 id를 받아 로그인 및 회원가입 중간 처리해주는 메소드",notes = "" +
            "js를 이용해 페북 로그인을 한뒤, 이 api로 로그인 연동id가 넘어오면, 이 id에 대해 회원가입 여부를 판단후, 로그인처리 또는" +
            " 회원가입 중간처리를 해준다.\n" +
            "요청 필드  ) \n" +
            "   snsId : 페북 로그인 연동 id \n")
    @PostMapping("/facebookLogin")
    public void facebookLogin(
            @RequestParam("snsId") String snsId,
            HttpServletResponse response
    ) throws IOException {
        SnsLoginResponseDto dto = memberService.snsLoginForFacebook(snsId);

        if(dto.getResultType().equals("signUp")){
            response.sendRedirect("/member/signup?memberId"+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
        }
        else{
            response.sendRedirect("");//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }*/

    /*naver 인증코드받는 메소드
     * - 인증코드를 받고 접근코드 요청을 보냄. */
    @ApiOperation(value = "naver 로그인 인증코드 받는 api",notes = "" +
            "처음 로그인하는 sns계정일 경우 ) \n" +
            "- 회원가입페이지로 리다이렉트.\n" +
            "- 리다이렉트url에 query string 파라미터로 'memberId' 전송. 현재 회원가입 진행중인 member의 id이다.  \n" +
            "이미 회원가입 되어있는 sns계정일 경우 )\n" +
            "- 메인페이지로 리다이렉트.\n" +
            "- ????? \n"
            /*"- 응답 필드 )\n" +
            "   snsType : String / sns종류\n" +
            "   resultType : String / sns연동 로그인 응답 종류\n" +
            "       ㄴ login : 해당 sns로 이미가입되어있어서 로그인이 됨을 의미. \n" +
            "       ㄴ signUp : 해당 sns로 가입되어있지않아 회원가입이 필요함을 의미. \n" +
            "   snsId : resultType이 'signUp'일 경우 값존재. sns로 로그인 연동시 sns으로부터 발급받는 id이다. 회원가입시 같이 넘겨주어야한다.\n" +
            "   sessionToken : resultType이 'login'인 경우 값존재. 로그인된 세션토큰이다. \n "*/)
    @RequestMapping("/naverAuthCode")
    public void getNaverAuthCode(
            @RequestParam(value = "code",required = false) String code,
            @RequestParam("state") String state,
            @RequestParam(value = "error",required = false) String error,
            @RequestParam(value = "error_description",required = false) String errorDescription,
            HttpServletResponse response
    ) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        SnsLoginResponseDto dto = memberService.snsLoginForNaver(code,state,error,errorDescription);

        if(dto.getResultType().equals("signUp")){
            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;
    }

    /*애플 로그인 받는 메소드*/
    @ApiOperation(value = "애플 로그인 리다이렉트 메소드",notes = "")
    @PostMapping("/appleAuthCode")
    public void getAppleAuthCode(
            AppleAuthCodeDto request,
            HttpServletResponse response
    ) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, ParseException, IllegalBlockSizeException {
        System.out.println("in /v2/api/appleAuthCode");
        SnsLoginResponseDto dto = memberService.snsLoginForApple(request.getId_token());

        if(dto.getResultType().equals("signUp")){
            response.sendRedirect("/cust/member/signup?memberId="+dto.getMemberId());//!!!!!리액트 서버에서 돌아가도록 세팅 필요.
        }
        else{
            response.sendRedirect("/cust/main/home?loggedIn=true&accesstoken="+dto.getSessionToken());//!!!!!sns로그인 완료후 보낼페이지 입력.
        }
        return;


//        String code = dto.getCode();
//        String client_secret = appleService.getAppleClientSecret(dto.getId_token());
//
//        logger.debug("================================");
//        logger.debug("id_token ‣ " + dto.getId_token());
//        logger.debug("payload ‣ " + appleService.getPayload(dto.getId_token()));
//        logger.debug("client_secret ‣ " + client_secret);
//        logger.debug("================================");
//
//        return appleService.requestCodeValidations(client_secret, code, null);
    }

    /*사용자 프로필 정보 페이지 조회
     * - 프로필 조회 요청이 들어오면, 보고자하는 프로필의 member가 자기자신인지, 다른 일반 사용자인지, 업체인지에 따라 조금씩
     *   다른 정보가 들어있는 DTO를 반환해준다. */
    @ApiOperation(value = "사용자 프로필 정보 조회",notes = "상대방 프로필을 클릭할시 해당 사용자의프로필 정보를 가져오는 api. \n" +
            "- 클릭한 프로필의 사용자가 자기자신인지, 업주 회원인지, 일단 다른 회원인지에 따라 조금씩 다른 정보를 반환한다. \n" +
            "- 요청 필드 )\n" +
            "   userId : 프로필을 보고자하는 회원의 id \n" +
            "   헤더에 세션 토큰 (선택)\n" +
            "- 응답 필드 ) \n" +
            "   memberId : Long / 회원id \n" +
            "   nickName : String / 회원 닉네임 \n" +
            "   profileImage : String / 회원 프로필 이미지 url\n" +
            "   backgroundImage : String / 배경이미지 url" +
            "   postCount : Integer / 작성글수. 리뷰, 조항일지, 조행기 등 본인이 작성한 글의 개수. \n" +
            "   likeCount : Integer / 좋아요수. 업체회원일경우, 글 등에서 받은 좋아요수 / 일반회원일경우, 회원이 좋아요 누른 수.\n" +
            "   isMe : boolean / 현재 프로필이 내 프로필인지 여부. \n" +
            "   takeCount : Integer / 업체 찜 수. 업체회원인경우, 받은 찜수. / 일반회원인경우, 회원이 찜한 수.\n" +
            "   isCompany : boolean / 업체의 프로필인지 여부. \n" +
            "   companyId : Long / 업체의 id\n")
    @GetMapping("/profile")
    @ResponseBody
    public UserProfileDTO getUserProfile(
            @RequestParam("userId") Long userId,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return memberService.getUserProfile(userId, token);
    }

    /*상대방 글 보기 api*/
    @ApiOperation(value = "상대방 글 보기",notes = "" +
            "요청 필드 ) \n" +
            "- userId : 상대방 id\n" +
            "- 헤더에 세션토큰 (선택)\n" +
            "응답 필드 )\n" +
            "- id : 게시글의 id \n" +
            "- profileImage : 작성자 프로필 사진 download url \n" +
            "- address : 게시글의 대상 선상의 주소 \n" +
            "- shipId : 게시글 대상 상품의 선상id\n" +
            "- memberId : 작성자 id \n" +
            "- nickName : 작성자 닉네임\n" +
            "- fishingType : 선상인지 갯바위인지 \n" +
            "\tㄴ ship : 선상\n" +
            "\tㄴ sealocks : 갯바위\n " +
            "- isLikeTo : 게시글에 대한 좋아요 여부\n" +
            "- createdDate : 작성일자 \n" +
            "- likeCount : 좋아요 수 \n" +
            "- commentCount : 댓글 수 \n" +
            "- scrapCount : 스크랩 수 \n" +
            "- title : 글 제목\n" +
            "- contents : 게시글 내용(일부만 출력)\n" +
            "- fishingDiaryType : String / 글이 조항일지인지, 유저조행기인지\n" +
            "- fileList : 이미지 파일 download url 리스트")
    @GetMapping("/profile/fishingDiary/{page}")
    @ResponseBody
    public Page<FishingDiaryDtoForPage> getUserFishingDiary(
            @RequestParam("userId") Long userId,
            @PathVariable("page") int page,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return memberService.getUserFishingDiary(userId,page,token);
    }

    /*프로필 관리 페이지 조회.
     * - member의 프로필이미지, uid, nickName, 상태메세지, 휴대폰번호, 이메일 정보가 든 dto반환. */
    @ApiOperation(value = "프로필 관리 페이지 조회",notes = "프로필 관리를 위한 자기자신의 프로필 정보를 가져온다. " +
            "요청 필드 ) \n" +
            "- 헤더에 세션토큰\n" +
            "응답 필드 ) \n" +
            "- id : Long / 회원의 id\n" +
            "- uid : String / 회원의 uid. 로그인할때 입력하는 id.\n" +
            "- memberName : String / 회원 실명\n" +
            "- email : String / 회원 email.\n" +
            "- profileImage : String / 프로필사진 url\n" +
            "- profileBackgroundImage : String / 프로필 배경 사진 url\n" +
            "- statusMessage : String / 상태메시지\n" +
            "- areaCode : String / 전화번호 앞부분(ex 010 ) \n" +
            "- localNumber : String / 전화번호 뒷부분 ( ex 12345678 )\n")
    @GetMapping("/profileManage")
    @ResponseBody
    public ProfileManageDTO getProfileManage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.getProfileManage(token);
    }

    /*프사변경
     * - !!!!!정확하게는 모르겠지만, 클라이언트가 자기사진을 올려서 요청을 보내는형식이 될거라고생각됨.
     * - 따라서 여기선 이미지파일을 파라미터로 받아서 프사로 업데이트해주는 기능 구현.
     * - 반환값 : 성공,실패여부 */
    @ApiOperation(value = "사용자 프로필사진 변경")
    @PutMapping("/profileManage/profileImage")
    @ResponseBody
    public boolean updateProfileImage(
            @RequestBody ModifyingProfileImgDto dto,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileImage(dto,sessionToken);
    }
    /*프사 없애기.*/
    @ApiOperation(value = "프사 없애기",notes = "" +
            "- 프로필 사진은 내립니다. \n" +
            "- 반환 ) 성공시 : true / 실패시 : false ")
    @PutMapping("/profileManage/noProfileImage")
    @ResponseBody
    public boolean deleteProfileImage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.deleteProfileImage(token);
    }

    /*프로필 배경 이미지 변경*/
    @ApiOperation(value = "프사 배경이미지 변경", notes = "")
    @PutMapping("/profileManage/profileBackgroundImage")
    @ResponseBody
    public boolean updateProfileBackgroundImage(
            @RequestBody ModifyingProfileImgDto dto,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileBackgroundImage(dto,sessionToken);
    }
    /*프사배경이미지 없애기.*/
    @ApiOperation(value = "프사배경이미지 없애기",notes = "" +
            "- 프로필 배경사진을 내립니다. \n" +
            "- 반환 ) 성공시 : true / 실패시 : false ")
    @PutMapping("/profileManage/noProfileBackgroundImage")
    @ResponseBody
    public boolean deleteProfileBackgroundImage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.deleteProfileBackgroundImage(token);
    }

    /*닉네임 변경*/
    @ApiOperation(value = "닉네임 변경")
    @PutMapping("/profileManage/nickName")
    @ResponseBody
    public String modifyProfileNickName(
            @RequestBody ModifyingNickNameDto nickName,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileNickName(sessionToken, nickName);
    }

    /*상태 메세지 변경*/
    @ApiOperation(value = "상태메세지 변경")
    @PutMapping("/profileManage/statusMessage")
    @ResponseBody
    public String modifyProfileStatusMessage(
            @RequestBody ModifyingStatusMessageDto statusMessage,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileStatusMessage(sessionToken, statusMessage);
    }

    /*이메일 변경*/
    @ApiOperation(value = "이메일 변경",notes = "" +
            "- 이메일만 변경되며, 로그인시 사용하는 uid는 변경되지 않습니다. ")
    @PutMapping("/profileManage/email")
    @ResponseBody
    public String modifyProfileEmail(
            @RequestBody ModifyingEmailDto email,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileEmail(sessionToken, email);
    }

    /*비번 변경
     * - 현재 비밀번호와 변경할 비번이 들어온다. */
    @ApiOperation(value = "비번 변경")
    @PutMapping("/profileManage/password")
    @ResponseBody
    public boolean modifyProfilePassword(
            @RequestBody ModifyProfilePwDto dto,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfilePassword(sessionToken, dto.getCurrentPw(),dto.getNewPw());
    }

    /*탈퇴하기
     * 삭제처리된 member의 id를 반환. */
    @ApiOperation(value = "탈퇴하기")
    @DeleteMapping("/profileManage/delete")
    @ResponseBody
    public Long deleteMember(
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.inactivateMember(sessionToken);
    }

    /*휴대폰 번호 변경 문자인증요청*/
    /*@ApiOperation(value="휴대폰 번호 변경 문자인증api", notes = "" +
            "- 휴대폰 번호 변경을 위한 문자인증 요청 api")
    @PostMapping("/profileManage/sendSms")
    public Long sendSmsForModifyingPhoneNumber(@RequestBody PhoneAuthForModifyPhoneNumber dto, @RequestHeader("Authorization") String token){
        PhoneAuthDto phoneAuthDto = PhoneAuthDto.builder().areaCode(dto.getAreaCode()).localNumber(dto.getLocalNumber()).build();
        return memberService.sendSmsForSignup(phoneAuthDto);
    }*/
    /*휴대폰 번호 변경*/
    /*@ApiOperation(value = "휴대폰 번호 변경 api",notes = "" +
            "- 문자인증 후, 휴대폰 번호 변경하는 api\n" +
            "- 반환 ) 변경된 번호. ")
    @PutMapping("/profileManage/phoneNumber")
    public String modifyPhoneNumber(@RequestBody ModifyPhoneNumberDto dto, @RequestHeader("Authorization") String token){

    }*/

    /*휴대폰 번호 변경을 위한 문자인증 요청.*/
    @ApiOperation(value = "휴대폰 번호 변경을 위한 문자인증 요청",notes = "" +
            "")
    @PostMapping("/profileManage/phoneNumber/sendSms")
    @ResponseBody
    public Long requestSmsAuthForModifyPhoneNum(@RequestBody PhoneAuthDto dto){
        return memberService.sendSmsAuthForModifyPhoneNum(dto);
    }
    /*문자인증 후, 휴대폰 번호 변경*/
    @ApiOperation(value = "문자인증 후, 휴대폰 번호 변경",notes = "")
    @PutMapping("/profileManage/phoneNumber")
    @ResponseBody
    public boolean modifyPhoneNumber(@RequestBody @Valid ModifyPhoneNumberDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.modifyPhoneNumber(dto,token);
    }

}
