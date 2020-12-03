package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.model.fishing.CompanyListDTO;
import com.tobe.fishking.v2.model.fishing.CompanyUpdateDTO;
import com.tobe.fishking.v2.model.fishing.CompanyWriteDTO;
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

import java.io.IOException;
import java.util.List;

@Api(tags = {"업체"})
@RequiredArgsConstructor
@RequestMapping(value = "/v1/api")
@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;

    /*업체 등록 요청
     * 반환 : 등록한 Company entity의 id.
     */
    @ApiOperation(value = "업체 등록")
    @PostMapping("/company")
    public Long handleCompanyRegisterReq(CompanyWriteDTO companyWriteDTO, @RequestParam("files") MultipartFile[] files) throws Exception {
        return companyService.handleCompanyRegisterReq(companyWriteDTO, files);
    }

    /*업체 등록 요청 수정*/
    @ApiOperation(value = "업체 등록 요청 수정")
    @PostMapping("/company/modify")
    public Long updateCompanyRegisterReq(CompanyWriteDTO companyWriteDTO, @RequestParam("files") MultipartFile[] files) throws Exception {
        return companyService.updateCompanyRegisterReq(companyWriteDTO, files);
    }

    /*업체 조회*/
    @ApiOperation(value = "업체 하나 조회")
    @GetMapping("/company")
    public CompanyDTO getCompany(Long companyId) throws ResourceNotFoundException {
        return companyService.getCompany(companyId);
    }


    /*업체 등록 요청 리스트 조회
    * */
    @ApiOperation(value = "업체 등록 요청 목록 조회")
    @GetMapping("/companyRegisterRequest/{page}")
    public Page<CompanyListDTO> getCompanyRegisterRequestList(@PathVariable("page") int page) throws ResourceNotFoundException {
        return companyService.getCompanyRegisterRequestList(page);
    }

    /*업체 등록 요청 취소*/
    @ApiOperation(value = "업체 등록 요청 취소")
    @DeleteMapping("/company")
    public Long deleteCompanyRegisterRequest(Long companyId) throws ResourceNotFoundException {
        return companyService.deleteCompanyRegisterRequest(companyId);
    }

}
