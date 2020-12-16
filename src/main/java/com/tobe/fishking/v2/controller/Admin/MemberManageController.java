package com.tobe.fishking.v2.controller.Admin;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.MemberDetailDtoForManager;
import com.tobe.fishking.v2.model.admin.MemberManageDtoForPage;
import com.tobe.fishking.v2.model.admin.MemberSearchConditionDto;
import com.tobe.fishking.v2.service.admin.MemberManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Api(tags={"회원관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class MemberManageController {

    @Autowired
    MemberManageService memberManageService;

    @ApiOperation(value = "회원 목록 검색",notes = "검색조건에 포함시킬 필드들만 값을 채우고 아닐경우 null로 비워두면된다. " +
            "uid,닉네임,이메일,snsId 필드만 패턴검색이 가능하다. ")
    @GetMapping("/manage/member/list/{page}")
    public Page<MemberManageDtoForPage> getMemberList(
            MemberSearchConditionDto conditionDto,
            @PathVariable("page") int page
    )
            throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return memberManageService.getMemberList(conditionDto,page);
    }

    /*회원 생성*/
    @ApiOperation(value = "회원 생성 기능",notes = "email,gender,roles,uid,isActive,isCertified,areaCode,localNumber 필수. ")
    @PostMapping("/manage/member/create")
    public MemberDetailDtoForManager makeMember(@RequestBody @Valid MemberDetailDtoForManager dto){
        return memberManageService.makeMember(dto);
    }

    /*회원 삭제
    * - 넘어온 id에 해당하는 회원을 '비활성화'처리 */
    @ApiOperation(value="회원 삭제(비활성화)")
    @DeleteMapping("/manage/member/delete")
    public Long inactivateMemberForManager(@RequestBody Long memberId) throws ResourceNotFoundException {
        return memberManageService.inactivateMemberForManager(memberId);
    }

    /*회원 상세보기*/
    @ApiOperation(value = "회원 상세보기")
    @GetMapping("/manage/member/detail")
    public MemberDetailDtoForManager showMemberDetail(@RequestParam("memberId") Long memberId) throws ResourceNotFoundException {
        return memberManageService.showMemberDetail(memberId);
    }

    /*회원 수정*/
    @ApiOperation(value = "회원 수정",notes = "email,gender,roles,uid,isActive,isCertified,areaCode,localNumber 필수")
    @PutMapping("/manage/member/update")
    public boolean updateMember(@RequestBody MemberDetailDtoForManager dto) throws ResourceNotFoundException {
        return memberManageService.modifyMember(dto);
    }

}
