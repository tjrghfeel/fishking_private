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
@RequestMapping(value = "/v2/api")
public class CommonController {
    @Autowired
    ResponseService responseService;
    @Autowired
    CommonService commonService;

    /*CodeGroup 추가 api*/
    @ApiOperation(value = "CodeGroup 추가", notes = "id, modifiedBy 필드 생략가능")
    @PostMapping("/codeGroup")
    public Long makeCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        return commonService.writeCodeGroup(codeGroupWriteDTO);
    }
    /*CodeGroup 수정 api*/
    @ApiOperation(value = "CodeGroup 수정", notes = "createdBy 필드 생략가능")
    @PutMapping("/codeGroup")
    public Long updateCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        return commonService.updateCodeGroup(codeGroupWriteDTO);
    }

    /*CommonCode 추가해주는 api.*/
    @ApiOperation(value = "Common code 추가", notes = "createdBy, retValue1, level, active, orderBy 필드 필수. ")
    @PostMapping("/commonCode")
    public String makeCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        return commonService.writeCommonCode(commonCodeWriteDTO);
    }
    /*CommonCode 수정 api*/
    @ApiOperation(value = "CommonCode 수정", notes = "id, modifiedBy, retValue1, level, active, orderBy 필드 필수. ")
    @PutMapping("/commonCode")
    public String updateCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        return commonService.updateCommonCode(commonCodeWriteDTO);
    }

    /*Code Group에 맞는 CommonCode목록을 반환해주는 api. */
    /*!!!!!common code들에 대한 설명 써두기, api검색을 db의 id가 아닌 스트링으로 하도록 변경 고려. */
    @ApiOperation(value = "Common Code 목록 출력", notes = "Common Group에 해당하는 CommonCode목록 출력. 다음은 code group id 목록이다. " +
            "80 : 어종 " +
            "82 : 지역 " +
            "83 : 시/군/구 " +
            "84 : 낚시기법 " +
            "85 : 서비스 " +
            "86 : 가격 " +
            "87 : 편의시설 " +
            "'시군구'의 common code들은 remark필드에 해당 '시군구'가 속하는 지역을 나타내는 '지역' common code의 code가 들어있다. " +
            "ex) '시군구'의 common code '인천 중구'는 수도권에 포함된 지역이다. 따라서 '인전 중구'common code의 remark필드에는" +
            "'지역' code group에서 '수도권' common code의 code가 들어있다. ")
    @GetMapping("/commonCode/{groupId}")
    public ListResult<CommonCodeDTO> getCommonCodeList(@PathVariable("groupId") Long groudId) throws ResourceNotFoundException {

        //서비스로부터 groupId에 해당하는 commonCodeDTO 리스트 받아오기.
        List<CommonCodeDTO> commonCodeDTOList = commonService.getCommonCodeDTOList(groudId);

        //ListResult로 만들어주기.
        return responseService.getListResult(commonCodeDTOList);
    }


}
