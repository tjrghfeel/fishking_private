package com.tobe.fishking.v2.controller.Admin;

import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.admin.company.CompanySearchConditionDto;
import com.tobe.fishking.v2.service.admin.CompanyManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
    @ApiOperation(value = "업체목록검색",notes = "정렬기준으로는 조건으로 넘기는 필드이름 중 하나를 sort인자에 그대로 넘기면 됩니다. \n" +
            "- createdDayStart, createdDayEnd는 그대로입력하지않고 createdDate로 입력합니다. 기본적으로 내림차순 정렬됩니다. \n" +
            "- memberName으로는 정렬은 되지않고 같은것끼리 묶여서 나옵니다. ")
    @GetMapping("/manage/company/list/{page}")
    public Page<CompanyManageDtoForPage> getCompanyList(
            @PathVariable("page") int page,
            CompanySearchConditionDto dto
    ) throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return companymanageService.getCompanyList(dto,page);
    }

}
