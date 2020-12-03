package com.tobe.fishking.v2.controller.member;

import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.auth.LoginDTO;
import com.tobe.fishking.v2.model.auth.ProfileManageDTO;
import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.model.auth.UserProfileDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.AttributeConverter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.IOException;

@Api(tags={"프로필"})
@RestController
@RequestMapping("/v1/api")
public class MemberController {

    @Autowired
    MemberService memberService;

    /*회원가입 - 휴대폰인증*/
    @ApiOperation(value = "휴대폰 인증")
    @PostMapping("/smsAuthReq")
    public boolean requestSmsAuth(@ModelAttribute("pNum") PhoneNumber pNum){
        return memberService.requestSmsAuth(pNum);
    }
    /*문자인증 확인*/
    @ApiOperation(value = "문자인증확인")
    @PostMapping("/checkSmsAuth")
    public boolean checkSignUpSmsAuth(
            @RequestParam("authNum") String authNum,
            @RequestParam("pNum") PhoneNumber pNum
    ){
        return memberService.checkSmsAuth(pNum,authNum);
    }

    /*회원가입 - 회원정보입력
    * - 이메일이 중복되지 않으면 true, 세션에 회원가입정보 저장.
    * - 중복되면 false반환.  */
    @ApiOperation(value = "회원가입 - 회원정보입력", notes = "이메일 : 이메일 형식을 지켜야함 / pw : 8~14자, 영문,숫자,특수문자포함 / nickName : 4~10자. " +
            "회원가입시 입력한 회원정보와 휴대폰인증정보가 함께넘어옴.")
    @PostMapping("/signUp/info")
    public boolean checkEmailDup(@ModelAttribute("info") @Valid SignUpDto signUpDto) {
        return memberService.signUp(signUpDto);
    }

    /*비밀번호 찾기(재설정) 인증.
    * - 번호누르고 발송버튼 누르면 들어오는 요청. */
    @ApiOperation(value = "비밀번호 찾기(재설정)")
    @PostMapping("/findPw/smsAuthReq")
    public boolean requestSmsAuthForPwSearch(@ModelAttribute("pNum") PhoneNumber pNum){
        if(memberService.checkExistByPhoneNum(pNum)){
            return memberService.requestSmsAuth(pNum);
        }
        else return false;
    }
    
    /*비밀번호 찾기 이후, 재설정.
    * - 핸드폰인증 후 뜨는 비번변경창에서 새 비밀번호입력후 변경클릭하면 들어오는 요청.
    * - 세션토큰에 해당하는 member의 비번을 새 비밀번호로 변경*/
    @ApiOperation(value = "비밀번호 변경")
    @PutMapping("/findPw/updatePw")
    public boolean updatePw(
            @RequestParam("newPw")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$", message = "pw형식이 잘못되었습니다") String pw,
            @RequestParam("phoneNum") PhoneNumber pNum
    ) {
        return memberService.updatePw(pw, pNum);
    }

    /*로그인*/
    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public Boolean login(@ModelAttribute("info") @Valid LoginDTO loginDTO, HttpServletResponse response){
        String sessionToken = memberService.login(loginDTO);
        if(sessionToken==null) return false;
        response.setHeader("token", sessionToken);
        return true;
    }

    /*로그아웃*/
    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    public boolean logout(@RequestParam("sessionToken") String sessionToken) throws ResourceNotFoundException {
        return memberService.logout(sessionToken);
    }

    /*사용자 프로필 정보 페이지 조회
    * - 프로필 조회 요청이 들어오면, 보고자하는 프로필의 member가 자기자신인지, 다른 일반 사용자인지, 업체인지에 따라 조금씩
    *   다른 정보가 들어있는 DTO를 반환해준다. */
    @ApiOperation(value = "사용자 프로필 정보 조회")
    @GetMapping("/profile")
    public UserProfileDTO getUserProfile(@RequestParam("userId") Long userId, @RequestParam("myId") Long myId) throws ResourceNotFoundException {
        return memberService.getUserProfile(userId, myId);
    }

    /*프로필 관리 페이지 조회.
    * - member의 프로필이미지, uid, nickName, 상태메세지, 휴대폰번호, 이메일 정보가 든 dto반환. */
    @ApiOperation(value = "프로필 관리 페이지 조회")
    @GetMapping("/profileManage")
    public ProfileManageDTO getProfileManage(@RequestParam("memberId") Long memberId) throws ResourceNotFoundException {
        return memberService.getProfileManage(memberId);
    }

    /*프사변경
     * - !!!!!정확하게는 모르겠지만, 클라이언트가 자기사진을 올려서 요청을 보내는형식이 될거라고생각됨.
     * - 따라서 여기선 이미지파일을 파라미터로 받아서 프사로 업데이트해주는 기능 구현.
     * - 반환값 : 성공,실패여부 */
    @ApiOperation(value = "사용자 프로필사진 변경")
    @PutMapping("/profileManage/profileImage")
    public boolean updateProfileImage(
            @RequestParam("profileImage") MultipartFile multipartFile,
            @RequestParam("memberId") Long memberId
    ) throws Exception {
        return memberService.updateProfileImage(multipartFile,memberId);
    }

    /*닉네임 변경*/
    @ApiOperation(value = "닉네임 변경")
    @PutMapping("/profileManage/nickName")
    public String modifyProfileNickName(
            @RequestParam("memberId") Long memberId,
            @RequestParam("nickName") String nickName
    ) throws ResourceNotFoundException {
        return memberService.modifyProfileNickName(memberId, nickName);
    }

    /*상태 메세지 변경*/
    @ApiOperation(value = "상태메세지 변경")
    @PutMapping("/profileManage/statusMessage")
    public String modifyProfileStatusMessage(
            @RequestParam("memberId") Long memberId,
            @RequestParam("statusMessage") String statusMessage
    ) throws ResourceNotFoundException {
        return memberService.modifyProfileStatusMessage(memberId, statusMessage);
    }

    /*이메일 변경*/
    @ApiOperation(value = "이메일 변경")
    @PutMapping("/profileManage/email")
    public String modifyProfileEmail(
            @RequestParam("memberId") Long memberId,
            @RequestParam("email") String email
    ) throws ResourceNotFoundException {
        return memberService.modifyProfileEmail(memberId, email);
    }

    /*비번 변경
    * - 현재 비밀번호와 변경할 비번이 들어온다. */
    @ApiOperation(value = "비번 변경")
    @PutMapping("/profileManage/password")
    public boolean modifyProfilePassword(
            @RequestParam("memberId") Long memberId,
            @RequestParam("currentPw") String currentPw,
            @RequestParam("newPw") String newPw
    ) throws ResourceNotFoundException {
        return memberService.modifyProfilePassword(memberId, currentPw, newPw);
    }

    /*탈퇴하기
    * 삭제처리된 member의 id를 반환. */
    @ApiOperation(value = "탈퇴하기")
    @DeleteMapping("/profileManage/delete")
    public Long deleteMember(
        @RequestParam("memberId") Long memberId
    ) throws ResourceNotFoundException {
        return memberService.deleteMember(memberId);
    }

}
