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

import javax.persistence.AttributeConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;

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

    /*문자인증 확인
    * - 인증번호 보내면 일치하는지 확인. */
    @ApiOperation(value = "문자인증확인")
    @PostMapping("/checkSmsAuth")
    public boolean checkSignUpSmsAuth(
            @RequestBody PhoneAuthCheckDto dto
    ){
        return memberService.checkSmsAuth(dto.getAreaCode(),dto.getLocalNumber(),dto.getAuthNum());
    }

    /*회원가입 - 회원정보입력
    * - 이메일이 중복되지 않으면 true, 세션에 회원가입정보 저장.
    * - 중복되면 false반환.  */
    @ApiOperation(value = "회원가입 - 회원정보입력", notes = "이메일 : 이메일 형식을 지켜야함 / pw : 8~14자, 영문,숫자,특수문자포함 / nickName : 4~10자. " +
            "회원가입시 입력한 회원정보와 휴대폰인증정보가 함께넘어옴.")
    @PostMapping("/signUp/info")
    public boolean checkEmailDup(@RequestBody @Valid SignUpDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    /*비밀번호 찾기(재설정) 인증.
    * - 번호누르고 발송버튼 누르면 들어오는 요청. */
    /*@ApiOperation(value = "비밀번호 찾기(재설정)")
    @PostMapping("/findPw/smsAuthReq")
    public boolean requestSmsAuthForPwSearch(){
        if(memberService.checkExistByPhoneNum(pNum.getAreaCode(),pNum.getLocalNumber())){
            return memberService.requestSmsAuth(pNum);
        }
        else return false;
    }*/
    
    /*비밀번호 찾기 이후, 재설정.
    * - 핸드폰인증 후 뜨는 비번변경창에서 새 비밀번호입력후 변경클릭하면 들어오는 요청.
    * - 세션토큰에 해당하는 member의 비번을 새 비밀번호로 변경*/
    @ApiOperation(value = "비밀번호 변경")
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

    /*사용자 프로필 정보 페이지 조회
    * - 프로필 조회 요청이 들어오면, 보고자하는 프로필의 member가 자기자신인지, 다른 일반 사용자인지, 업체인지에 따라 조금씩
    *   다른 정보가 들어있는 DTO를 반환해준다. */
    @ApiOperation(value = "사용자 프로필 정보 조회",notes = "상대방 프로필을 클릭할시 해당 사용자의프로필 정보를 가져오는 api. \n" +
            "- 클릭한 프로필의 사용자가 자기자신인지, 업주 회원인지, 일단 다른 회원인지에 따라 조금씩 다른 정보를 반환한다. ")
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
            @RequestParam("profileImage") MultipartFile multipartFile,
            HttpServletRequest request
    ) throws Exception {
        String sessionToken = request.getHeader("Authorization");
        return memberService.updateProfileImage(multipartFile,sessionToken);
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
