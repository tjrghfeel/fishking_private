package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.HarborDetailDto;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.fishing.AddHarborDto;
import com.tobe.fishking.v2.service.fishking.HarborService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags={"항구"})
@RequestMapping("/v2/api")
public class HarborController {
    @Autowired
    HarborService harborService;

    //항구 생성
    @ApiOperation(value = "항구 생성",
            notes = ""
    )
    @PostMapping("/harbor")
    public void createHarbor(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid AddHarborDto dto
    ) throws ServiceLogicException {
        harborService.addHarbor(dto, token);

        return;

    }

    //항구 정보 조회
    @ApiOperation(value = "항구 조회",
            notes = "response )\n" +
                    "- id : Long / 항구 id\n" +
                    "- name : String / 항구명\n" +
                    "- sido : String / 주소(시,도)\n" +
                    "- gungu : String / 주소(군,구)\n" +
                    "- address : String / 주소(도로명)\n" +
                    "- lat : Double / 위도\n" +
                    "- lon : Double / 경도\n" +
                    "- observerCode : String / 매핑된 관측소 코드\n" +
                    "- observerId : Long / 매핑된 관측소 id\n" +
                    "- adtId : String / 항구 카메라 id\n" +
                    "- adtPw : String / 항구 카메라 pw\n"
    )
    @GetMapping("/harbor/{harborId}")
    public HarborDetailDto getHarborDetail(
            @PathVariable("harborId") Long harborId,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return harborService.getHarborDetail(harborId, token);
    }


}
