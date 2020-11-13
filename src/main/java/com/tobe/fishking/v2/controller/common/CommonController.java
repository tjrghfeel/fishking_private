package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.CodeGroupWriteDTO;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Common")
@RestController
@RequestMapping(value = "/v1/api")
public class CommonController {
    @Autowired
    ResponseService responseService;
    @Autowired
    CommonService commonService;

    /*CodeGroup 추가 api*/
    @ApiOperation(value = "CodeGroup 추가", notes = "id, modifiedBy 필드 생략가능")
    @PostMapping("/codeGroup")
    public Long makeCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        return commonService.writeCodeGroup(codeGroupWriteDTO);
    }
    /*CodeGroup 수정 api*/
    @ApiOperation(value = "CodeGroup 수정", notes = "createdBy 필드 생략가능")
    @PutMapping("/codeGroup")
    public Long updateCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        return commonService.updateCodeGroup(codeGroupWriteDTO);
    }

    /*CommonCode 추가해주는 api.*/
    @ApiOperation(value = "Common code 추가", notes = "createdBy, retValue1, level, active, orderBy 필드 필수. ")
    @PostMapping("/commonCode")
    public String makeCommonCode(CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        return commonService.writeCommonCode(commonCodeWriteDTO);
    }
    /*CommonCode 수정 api*/
    @ApiOperation(value = "CommonCode 수정", notes = "id, modifiedBy, retValue1, level, active, orderBy 필드 필수. ")
    @PutMapping("/commonCode")
    public String updateCommonCode(CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        return commonService.updateCommonCode(commonCodeWriteDTO);
    }

    /*Code Group에 맞는 CommonCode목록을 반환해주는 api. */
    @ApiOperation(value = "Common Code 목록 출력", notes = "Common Group에 해당하는 CommonCode목록 출력")
    @GetMapping("/commonCode/{groupId}")
    public ListResult<CommonCodeDTO> getCommonCodeList(@PathVariable("groupId") Long groudId) throws ResourceNotFoundException {

        //서비스로부터 groupId에 해당하는 commonCodeDTO 리스트 받아오기.
        List<CommonCodeDTO> commonCodeDTOList = commonService.getCommonCodeDTOList(groudId);

        //ListResult로 만들어주기.
        return responseService.getListResult(commonCodeDTOList);
    }


}
