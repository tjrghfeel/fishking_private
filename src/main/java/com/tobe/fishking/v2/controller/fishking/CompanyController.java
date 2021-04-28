package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.model.admin.company.CompanyCreateDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanyModifyDtoForManage;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.service.admin.CompanyManageService;
import com.tobe.fishking.v2.service.fishking.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
//import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Api(tags = {"업체"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;
    @Autowired
    CompanyManageService companyManageService;

    /*업체 등록 요청
     * 반환 : 등록한 Company entity의 id.
     */
    @ApiOperation(value = "업체 등록",notes = "업체등록 요청 api.\n" +
            "- 요청 필드 ) \n" +
            "   세션토큰 필요.\n" +
            "   companyName : 업체명\n" +
            "   memberName : 회원명\n" +
            "   tel : 전화번호\n" +
            "   phoneNumber : 휴대폰번호" +
            "   bizNo : 사업자등록번호\n" +
            "   harbor : 항구명\n" +
            "   bank : 은행\n" +
            "   accountNo : 계좌번호\n" +
            "   companyAddress : 업체 지역명\n" +
            "   bizNoFile : 사업자등록증 이미지 파일\n" +
            "   representFile : 대표자 신분증 파일\n" +
            "   accountFile : 정산통장사본 이미지파일\n" +
            "   adtId : String / adt캡스 아이디\n" +
            "   adtPw : String / adt캡스 비번\n" +
            "   nhnId : String / nhn토스트캠 아이디\n" +
            "   nhnPw : String / nhn토스트캠 비번\n" +
            "- 응답 ) 등록대기중인 업체 id ")
    @PostMapping("/company")
    public Long handleCompanyRegisterReq(
            @RequestBody @Valid CompanyWriteDTO dto,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        return companyService.handleCompanyRegisterReq(dto,token);

        /*CompanyCreateDtoForManage dto = CompanyCreateDtoForManage.builder()
                .token(request.getHeader("Authorization"))
                .memberId(null)
                .companyName(request.getParameter("companyName"))
//                .memberName(request.getParameter("memberName"))
                .sido(null)
                .gungu(null)
                .tel(request.getParameter("tel"))
                .bizNo(request.getParameter("bizNo"))
                .harbor(request.getParameter("harbor"))
                .bank(request.getParameter("bank"))
                .accountNo(request.getParameter("accountNo"))
                .ownerWording("")
                .isOpen(false)
                .skbAccount(null)
                .skbPassword(null)
                .companyAddress(request.getParameter("companyAddress"))
                .isRegistered(false)
                .bizNoFile(request.getFile("bizNoFile"))
                .representFile(request.getFile("representFile"))
                .accountFile(request.getFile("accountFile"))
                .build();
        return companyManageService.createCompany(dto);*/
    }

    /*업체 등록 요청 수정*/
    @ApiOperation(value = "업체 등록 요청 수정",notes = "" +
            "- 요청 필드 ) \n" +
            "   세션토큰 필요.\n" +
            "   companyId : 수정할 업체 id\n" +
            "   companyName : 업체명\n" +
            "   memberName : 회원명\n" +
            "   tel : 전화번호\n" +
            "   bizNo : 사업자등록번호\n" +
            "   harbor : 항구명\n" +
            "   bank : 은행\n" +
            "   accountNo : 계좌번호\n" +
            "   companyAddress : 업체 주소\n" +
            "   bizNoFile : 사업자등록증 이미지 파일\n" +
            "   representFile : 대표자 신분증 파일\n" +
            "   accountFile : 정산통장사본 이미지파일\n" +
            "- 응답 ) 수정성공시 true. ")
    @PutMapping("/company/modify")
    public Long updateCompanyRegisterReq(
            CompanyUpdateDTO dto,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        return companyService.updateCompanyRegisterReq(dto,token);

        /*CompanyModifyDtoForManage dto = CompanyModifyDtoForManage.builder()
                .token(request.getHeader("Authorization"))
                .id(Long.parseLong(request.getParameter("id")))
                .memberId(null)
                .companyName(request.getParameter("companyName"))
//                .memberName(request.getParameter("memberName"))
                .sido(null)
                .gungu(null)
                .tel(request.getParameter("tel"))
                .bizNo(request.getParameter("bizNo"))
                .harbor(request.getParameter("harbor"))
                .bank(request.getParameter("bank"))
                .accountNo(request.getParameter("accountNo"))
                .ownerWording("")
                .isOpen(false)
                .skbAccount(null)
                .skbPassword(null)
                .companyAddress(request.getParameter("companyAddress"))
                .isRegistered(false)
                .bizNoFile(request.getFile("bizNoFile"))
                .representFile(request.getFile("representFile"))
                .accountFile(request.getFile("accountFile"))
                .build();
        return companyManageService.modifyCompany(dto);*/
    }

    /*업체 조회*/
    @ApiOperation(value = "업체 하나 조회",notes = "관리자용api")
    @GetMapping("/company")
    public CompanyDTO getCompany(Long companyId) throws ResourceNotFoundException {
        return companyService.getCompany(companyId);
    }

    /*업체 등록 요청 리스트 조회
    * */
    @ApiOperation(value = "업체 등록 요청 목록 조회",notes = "관리자용api.")
    @GetMapping("/companyRegisterRequest/{page}")
    public Page<CompanyListDTO> getCompanyRegisterRequestList(@PathVariable("page") int page) throws ResourceNotFoundException {
        return companyService.getCompanyRegisterRequestList(page);
    }

    /*업체 삭제*/
//    @ApiOperation(value = "업체 삭제",notes = "관리자용 api.")
//    @DeleteMapping("/company")
//    public Long deleteCompanyRegisterRequest(@RequestBody DeletingCompanyDto dto) throws ResourceNotFoundException {
//        return companyService.deleteCompanyRegisterRequest(dto.getCompanyId());
//    }

    /*업체 등록 취소*/
    @ApiOperation(value="업체 등록 취소",notes = "" +
            "- 업체등록요청한 현재 회원이 업체등록을 취소하려할때 사용.\n" +
            "- 요청 필드 ) \n" +
            "   세션토큰 필요.\n" +
            "- 응답 필드 )\n" +
            "   성공시 true. ")
    @DeleteMapping("/cancelCompanyRigster")
    public Boolean cancelCompanyRegister(@RequestHeader("Authorization") String token) throws ResourceNotFoundException, ServiceLogicException {
        return companyService.cancelCompanyRegister(token);
    }

    //해당 회원이 이미 업체등록요청을 했는지 여부
    @ApiOperation(value = "업체등록요청 여부",notes = "" +
            "해당 회원이 업체등록 요청을 했는지 여부를 확인")
    @GetMapping("/company/checkRequestExist")
    public Boolean checkRequestExist(
            @RequestHeader("Authorization") String token
    ){
        try {
            return companyService.checkRequestExist(token);
        } catch (Exception e) {
            return null;
        }
    }


}
