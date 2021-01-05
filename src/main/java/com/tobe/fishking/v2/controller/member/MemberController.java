package com.tobe.fishking.v2.controller.member;

import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.auth.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.RegEx;
import javax.persistence.AttributeConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Api(tags={"프로필"})
@RestController
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
    @ApiOperation(value = "문자인증 문자 보내기",notes = "" +
            "- 회원가입시, 문자 인증 문자를 보내고 해당 문자인증 건에 대한 id를 반환한다. 인증번호를 보낼때 이 id값을 같이 보내야 한다. " +
            "- 요청 필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 응답 ) 문자 인증 전송 성공시 true")
    @PostMapping("/sendSms")
    public boolean sendSms(@RequestBody PhoneAuthDto dto){
        return memberService.sendSmsForSingup(dto);
    }

    /*문자인증 확인
    * - 인증번호 보내면 일치하는지 확인. */
    @ApiOperation(value = "문자인증확인",notes = "" +
            "- 인증번호가 일치하는지 확인.\n" +
            "- 요청 필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "   authNum : 인증번호\n" +
            "- 반환 ) 일치할경우 true, 아닐경우 false. ")
    @PostMapping("/checkSmsAuth")
    public boolean checkSignUpSmsAuth(
            @RequestBody PhoneAuthCheckDto dto
    ){
        return memberService.checkSmsAuth(dto.getAreaCode(),dto.getLocalNumber(),dto.getAuthNum());
    }

    /*아이디 중복 확인*/
    @ApiOperation(value = "아이디 중복 확인",notes = "" +
            "- 입력한 아이디가 이미 존재하는지 확인\n" +
            "- 입력 필드 )\n" +
            "   uid : 중복확인할 아이디\n" +
            "- 반환 ) " +
            "   0 : 중복 안됨\n" +
            "   1 : 중복 \n" +
            "   2 : 이메일 형식에 맞지 않음")
    @GetMapping("/checkUidDup")
    public int checkUidDup(@RequestParam("uid") String uid){
        return memberService.checkUidDup(uid);
    }

    /*회원가입 - 회원정보입력
    * - 이메일이 중복되지 않으면 true, 세션에 회원가입정보 저장.
    * - 중복되면 false반환.  */
    @ApiOperation(value = "회원가입 - 회원정보입력", notes = "" +
            "- 회원정보와 함께 보낼경우 회원가입 처리해주는 api \n" +
            "- 요청 필드 ) \n" +
            "   email : String / 이메일. 이메일 형식에 맞아야한다\n" +
            "   pw : String / 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "   nickName : String / 닉네임. 4~10자. " +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 반환 ) 회원가입처리 성공시 true.  ")
    @PostMapping("/signUp/info")
    public boolean checkEmailDup(@RequestBody @Valid SignUpDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    /*비밀번호 찾기(재설정) 인증.
    * - 번호누르고 발송버튼 누르면 들어오는 요청. */
    @ApiOperation(value = "비밀번호 찾기(재설정)",notes = "" +
            "- 비밀번호 재설정시 문자인증하는 api. \n" +
            "- 요청필드 )\n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)\n" +
            "- 응답 ) 성공시 true ")
    @PostMapping("/findPw/smsAuthReq")
    public boolean requestSmsAuthForPwSearch(@RequestBody PhoneAuthDto dto){
        return memberService.sendSmsForPwReset(dto);
    }
    /*비밀번호 찾기 이후, 재설정.
    * - 핸드폰인증 후 뜨는 비번변경창에서 새 비밀번호입력후 변경클릭하면 들어오는 요청.
    * - 세션토큰에 해당하는 member의 비번을 새 비밀번호로 변경*/
    @ApiOperation(value = "비밀번호 변경",notes = "" +
            "- 비밀번호 재설정에서 문자인증 통과후, 비번을 재설정하는 api.\n" +
            "- 요청 필드 )\n" +
            "   newPw : String / 새 비밀번호. 8~14자, 영문,숫자,특수문자포함 \n" +
            "   areaCode : 지역번호 및 휴대폰 앞번호\n" +
            "   localNumber : 나머지 번호(공백,'-'없이 숫자만입력)")
    @PutMapping("/findPw/updatePw")
    public boolean updatePw(
            @RequestBody @Valid ResetPwDto resetPwDto
    ) {
        return memberService.updatePw(resetPwDto);
    }

    /*로그인*/
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginDTO loginDTO){
        return memberService.login(loginDTO);
    }

    /*로그아웃*/
    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    public boolean logout(HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.logout(sessionToken);
    }

    /*sns 인증 요청 결과
    * - 인증 요청에대해 카카오 인증서버에서 응답을 redirect하면 이를 받는 메소드.
    * - 에러인지 성공인지 판단하여 에러이면 프론트로 redirect.
    * - 성공이면 카카오 서버로 토큰 요청.
    * - 반환 ) 미정!!!!!*/
    @RequestMapping("/kakaoAuthCode")
    public void acceptKakaoAuthCode(/*HttpServletResponse response*/
            @RequestParam("code") String code,
            @RequestParam("error") String error//!!!!!어떤걸로 받아야 응답이 받아지는지 잘 모르겠다. 테스트하면서 해야할 필요.
    ){
        /*error인지 성공인지 확인*/

        /*에러이면 프론트 페이지로 리다이렉트*/

        /*성공이면, 카카오 서버로 토큰 요청. */

    }

    /*kakao 토큰 받는 메소드. */

    /*naver 인증코드받는 메소드
    * - 인증코드를 받고 접근코드 요청을 보냄. */
    @ApiOperation(value = "naver 로그인 인증코드 받는 api",notes = "")
    @RequestMapping("/naverAuthCode")
    public void getNaverAuthCode(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam("error") String error,
            @RequestParam("error_description") String errorDescription
    ) throws IOException {
        String url = "https://nid.naver.com/oauth2.0/token";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();
        parameter.put("grant_type","authorization_code");
        parameter.put("client_id","xQF6XDWPhMC665JO2kSq");
        parameter.put("client_secret","shKqzGtgR1");
        parameter.put("code",code);
        parameter.put("state","sample");
        parameter.put("refresh_token","");
        parameter.put("access_token","");
        parameter.put("service_provider","");

        sendRequest(url,method,parameter,"");
        return;
    }

    /*naver 접근코드 받는 메소드
    * - 접근코드로 회원정보를 요청.  */
    @ApiOperation(value = "",notes = "")
    @RequestMapping("/naverAccessToken")
    public void getAccessToken(
            @RequestParam("access_token") String accessToken,
//            @RequestParam("refresh_token") String refreshToken,
            @RequestParam("token_type") String tokenType,
            @RequestParam("expires_in") Integer expiresIn,
            @RequestParam("error") String error,
            @RequestParam("error_description") String errorDescription
    ) throws IOException {
        String url = "https://openapi.naver.com/v1/nid/me";
        String method = "POST";

        sendRequest(url,method,new HashMap<String,String>(),accessToken);
        return;
    }

    /*요청보내는 메소드*/
    public void sendRequest(String inputUrl, String method, Map<String,String> parameter, String token) throws IOException {
        URL url = new URL(inputUrl);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod(method); // PUT is another valid option
        http.setDoOutput(true);

        /*Map<String,String> arguments = new HashMap<>();
        arguments.put("username", "root");
        arguments.put("password", "sjh76HSn!"); // This is a fake password obviously*/
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
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        // Do something with http.getInputStream()
    }

    /*사용자 프로필 정보 페이지 조회
    * - 프로필 조회 요청이 들어오면, 보고자하는 프로필의 member가 자기자신인지, 다른 일반 사용자인지, 업체인지에 따라 조금씩
    *   다른 정보가 들어있는 DTO를 반환해준다. */
    @ApiOperation(value = "사용자 프로필 정보 조회",notes = "상대방 프로필을 클릭할시 해당 사용자의프로필 정보를 가져오는 api. \n" +
            "- 클릭한 프로필의 사용자가 자기자신인지, 업주 회원인지, 일단 다른 회원인지에 따라 조금씩 다른 정보를 반환한다. \n" +
            "- 요청 필드 )\n" +
            "   userId : 프로필을 보고자하는 회원의 id \n" +
            "- 응답 필드 ) \n" +
            "   memberId : 회원id \n" +
            "   nickName : 회원 닉네임 \n" +
            "   profileImage : 회원 프로필 이미지 download url\n" +
            "   isActive : 회원 활성화 여부 \n" +
            "   postCount : 회원의 작성글 수 \n" +
            "   likeCount : " +
            "- 자기자신의 프로필인 경우 isMe = true, 업체회원인 경우 isShip = true \n" +
            "- postCount : 프로필 회원이 작성한 FishingDiary 글의 개수 / takeCount : 프로필 회원이 삼품에 대해 찜한 개수 \n" +
            "- 업체 회원인 경우, 선상id(shipId), 선상명(shipName), 주소(시,도)(sido), 선상에서 잡는 어종(fishSpecies)," +
            " 선상이 받은 좋아요소수(likeCount) 정보가 추가된다. ")
    @GetMapping("/profile")
    public UserProfileDTO getUserProfile(@RequestParam("userId") Long userId, HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.getUserProfile(userId, sessionToken);
    }

    /*프로필 관리 페이지 조회.
    * - member의 프로필이미지, uid, nickName, 상태메세지, 휴대폰번호, 이메일 정보가 든 dto반환. */
    @ApiOperation(value = "프로필 관리 페이지 조회",notes = "프로필 관리를 위한 자기자신의 프로필 정보를 가져온다. ")
    @GetMapping("/profileManage")
    public ProfileManageDTO getProfileManage(HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.getProfileManage(sessionToken);
    }

    /*프사변경
     * - !!!!!정확하게는 모르겠지만, 클라이언트가 자기사진을 올려서 요청을 보내는형식이 될거라고생각됨.
     * - 따라서 여기선 이미지파일을 파라미터로 받아서 프사로 업데이트해주는 기능 구현.
     * - 반환값 : 성공,실패여부 */
    @ApiOperation(value = "사용자 프로필사진 변경")
    @PutMapping("/profileManage/profileImage")
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
    public boolean deleteProfileImage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return memberService.deleteProfileImage(token);
    }


    /*프로필 배경 이미지 변경*/
    @ApiOperation(value = "프사 배경이미지 변경", notes = "")
    @PutMapping("/profileManage/profileBackgroundImage")
    public boolean updateProfileBackgroundImage(
            @RequestBody ModifyingProfileImgDto dto,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileBackgroundImage(dto,sessionToken);
    }

    /*닉네임 변경*/
    @ApiOperation(value = "닉네임 변경")
    @PutMapping("/profileManage/nickName")
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
    public String modifyProfileStatusMessage(
            @RequestBody ModifyingStatusMessageDto statusMessage,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.modifyProfileStatusMessage(sessionToken, statusMessage);
    }

    /*이메일 변경*/
    @ApiOperation(value = "이메일 변경")
    @PutMapping("/profileManage/email")
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
    public Long deleteMember(
        HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return memberService.inactivateMember(sessionToken);
    }

    /**/


}
