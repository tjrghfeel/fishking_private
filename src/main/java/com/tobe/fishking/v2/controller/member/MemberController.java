package com.tobe.fishking.v2.controller.member;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.auth.ProfileManageDTO;
import com.tobe.fishking.v2.model.auth.UserProfileDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags={"프로필"})
@RestController
@RequestMapping("/v1/api")
public class MemberController {

    @Autowired
    MemberService memberService;

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
     * - 반환값 : memberId */
    @ApiOperation(value = "사용자 프로필사진 변경")
    @PostMapping("/profileManage/profileImage")
    public boolean updateProfileImage(
            @RequestParam("profileImage") MultipartFile multipartFile,
            @RequestParam("memberId") Long memberId
    ) throws ResourceNotFoundException, IOException {
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
    * !!!!! '자바 암호화' 적용?? */
    /*@ApiOperation(value = "비번 변경")
    @PutMapping("/profileManage/password")
    public boolean modifyProfilePassword(
            @RequestParam("memberId") Long memberId,
            @RequestParam("currentPw") String currentPw,
            @RequestParam("newPw") String newPw
    ) throws ResourceNotFoundException {
        return memberService.modifyProfilePassword(memberId, currentPw, newPw);
    }*/

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
