package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.PushAllDto;
import com.tobe.fishking.v2.model.admin.member.*;
import com.tobe.fishking.v2.service.admin.MemberManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.IOException;
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

    @ApiOperation(value = "회원 목록 검색",notes = "검색조건에 포함시킬 필드들만 값을 채우고 아닐경우 null로 비워두면된다. \n" +
            "- uid,닉네임,이메일,snsId 필드만 패턴검색이 가능하다. \n" +
            "- sort 필드에 검색 조건명 중 하나를 입력하면 해당 조건을 기준으로 내림차순 정렬이 된다. (가입일 기준으로 정렬하고자" +
            "   할 경우 'joinDt'를 주면된다) \n" +
            "- 이름, 전화번호, 주소로 정렬할 경우 내림차순으로 정렬되지않고, 같은 값끼리 연달아서 나온다. ")
    @GetMapping("/manage/member/list/{page}")
    public Page<MemberManageDtoForPage> getMemberList(
            @Valid MemberSearchConditionDto conditionDto,
            @PathVariable("page") int page
    )
            throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, ServiceLogicException {
        try{
            if(conditionDto.getRoles()!=null){Role.valueOf(conditionDto.getRoles());}
        }catch (Exception e){throw new ServiceLogicException("roles의 값이 잘못되었습니다.");}
        return memberManageService.getMemberList(conditionDto,page);
    }

    /*회원 생성*/
//    @ApiOperation(value = "회원 생성 기능",notes = "email,gender,roles,uid,isActive,isCertified,areaCode,localNumber 필수. ")
//    @PostMapping("/manage/member/create")
//    public MemberDetailDtoForManager makeMember(@RequestBody @Valid MemberDetailDtoForManager dto){
//        return memberManageService.makeMember(dto);
//    }

    /*회원 삭제
    * - 넘어온 id에 해당하는 회원을 '비활성화'처리 */
//    @ApiOperation(value="회원 삭제(비활성화)")
//    @DeleteMapping("/manage/member/delete")
//    public Long inactivateMemberForManager(@RequestBody DeletingMemberDtoForManage memberId) throws ResourceNotFoundException {
//        return memberManageService.inactivateMemberForManager(memberId.getMemberId());
//    }

    /*회원 상세보기*/
    @ApiOperation(value = "회원 상세보기")
    @GetMapping("/manage/member/detail")
    public MemberDetailDtoForManager showMemberDetail(@RequestParam("memberId") Long memberId) throws ResourceNotFoundException {
        return memberManageService.showMemberDetail(memberId);
    }

    /*회원 수정*/
//    @ApiOperation(value = "회원 수정",notes = "email,gender,roles,uid,isActive,isCertified,areaCode,localNumber 필수")
//    @PutMapping("/manage/member/update")
//    public boolean updateMember(@RequestBody MemberDetailDtoForManager dto) throws ResourceNotFoundException {
//        return memberManageService.modifyMember(dto);
//    }

    //세션토큰 조회
    @ApiOperation(value = "세션토큰 조회")
    @GetMapping("/manage/member/token/{memberId}")
    public String getSessionToken(
            @RequestHeader("Authorization") String token,
            @PathVariable("memberId") Long memberId
    ){
        return memberManageService.getSessionToken(token,memberId);
    }

    //임시 회원 생성
    @ApiOperation(value = "임시 회원 생성")
    @PostMapping("/manage/member/tempCreate")
    public Long makeTempMember(
            @RequestBody @Valid MakeTempMemberDto dto,
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        return memberManageService.makeTempMember(dto,token);
    }

    //계정 정지
    @ApiOperation(value = "계정 정지")
    @PutMapping("/manage/member/suspend/{memberId}/{suspend}")
    public Boolean setIsSuspended(
            @RequestHeader("Authorization") String token,
            @PathVariable("memberId") Long memberId,
            @PathVariable("suspend") String suspend
    ) throws ServiceLogicException {

        if(!suspend.equals("true") && !suspend.equals("false")){throw new ServiceLogicException("suspend의 값으로는 'true' 또는 'false'만 가능합니다.");}
        return memberManageService.setIsSuspended(token, memberId, suspend);
    }

    //비밀번호 초기화
//    @ApiOperation(value="비밀번호 초기화")
//    @PutMapping("/manage/member/initialPw")
//    public Boolean initialPw(
//            @RequestBody @Valid InitialPwDto dto,
//            @RequestHeader("Authorization") String token
//    ) throws ServiceLogicException {
//        return memberManageService.initialPw(dto, token);
//    }

    //전체 푸시알림
    @ApiOperation(value = "전체 푸시 알림")
    @PostMapping("/manage/member/pushAll")
    public Boolean pushAll(
            @RequestBody @Valid PushAllDto dto,
            @RequestHeader("Authorization") String token
    ) throws IOException, ServiceLogicException {
        return memberManageService.pushAll(dto, token);
    }

}
