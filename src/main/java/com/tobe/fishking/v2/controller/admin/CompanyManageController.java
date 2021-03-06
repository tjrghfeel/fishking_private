package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.company.CompanyCreateDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.admin.company.CompanyModifyDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanySearchConditionDto;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.service.admin.CompanyManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@Api(tags={"업체관리"})
@RequestMapping("/v2/api")
public class CompanyManageController {

    @Autowired
    CompanyManageService companymanageService;

    /*업체목록 검색*/
    @ApiOperation(value = "업체목록검색",notes = "검색조건에 포함시킬 필드들만 값을 채우고 아닐경우 null로 비워두면된다. \n" +
            "- 정렬기준으로는 조건으로 넘기는 필드이름 중 하나를 sort인자에 그대로 넘기면 됩니다. \n" +
            "- createdDayStart, createdDayEnd는 그대로입력하지않고 createdDate로 입력합니다. 기본적으로 내림차순 정렬됩니다. \n" +
            "- memberName으로정렬하면 이름순 정렬은 되지않고 단지 같은이름끼리 연달아서 나옵니다. ")
    @GetMapping("/manage/company/list/{page}")
    public Page<CompanyManageDtoForPage> getCompanyList(
            @PathVariable("page") int page,
            @Valid CompanySearchConditionDto  dto
    ) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return companymanageService.getCompanyList(dto,page);
    }

    /*업체 상세보기
    * */
    @ApiOperation(value="업체 상세보기",notes = "업체의 id를 넘기면 업체의 모든 정보 반환. ")
    @GetMapping("/manage/company/detail")
    public CompanyDTO getCompanyDetailForManage(@RequestParam("companyId") Long companyId) throws ResourceNotFoundException {
        return companymanageService.getCompanyDetail(companyId);
    }

    /*업체 생성
    * */
    @ApiOperation(value = "업체 생성", notes = "넘어온 데이터들로 업체를 생성하고 생성된 업체의 id반환. ")
    @PostMapping("/manage/company")
    public Long createCompany(
            /*MultipartHttpServletRequest request*/
            @RequestBody @Valid CompanyCreateDtoForManage dto,
            @RequestHeader("Authorization") String token
    ) throws Exception {
        return companymanageService.createCompany(dto, token);
    }

    /*업체 수정*/
    @ApiOperation(value="업체 수정",notes = "")
    @PutMapping("/manage/company")
    public Boolean modifyCompany(
            /*MultipartHttpServletRequest request*/
            @RequestBody @Valid CompanyModifyDtoForManage dto,
            @RequestHeader("Authorization") String token) throws Exception {
        return companymanageService.modifyCompany(dto, token);
    }

    //업체 등록 요청 승인
    @ApiOperation(value = "업체 등록 요청 승인")
    @PutMapping("/manage/company/acceptRequest/{companyId}")
    public Boolean requestAccept(
            @RequestHeader("Authorization") String token,
            @PathVariable("companyId") Long companyId
    ) throws ResourceNotFoundException {
        return companymanageService.acceptRequest(token, companyId);
    }
    //업체 등록 요청 반려
    @ApiOperation(value = "업체 등록 요청 반려")
    @DeleteMapping("/manage/company/rejectRequest/{companyId}")
    public Boolean rejectRequest(
            @RequestHeader("Authorization") String token,
            @PathVariable("companyId") Long companyId
    ) throws ResourceNotFoundException, ServiceLogicException {
        return companymanageService.rejectRequest(token, companyId);
    }

    //영업 상태 수정
    @ApiOperation(value = "영업 상태 수정")
    @PutMapping("/manage/company/isOpen/{companyId}/{isOpen}")
    public Boolean setIsOpen(
            @RequestHeader("Authorization") String token,
            @PathVariable("companyId") Long companyId,
            @PathVariable("isOpen") String isOpen
    ) throws ResourceNotFoundException {
        if(!isOpen.equals("true") && !isOpen.equals("false")){throw new RuntimeException("isOpen값은 'true' 또는 'false'이어야 합니다.");}
        return companymanageService.setIsOpen(token, companyId, isOpen);
    }

    //


}
