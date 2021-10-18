package com.tobe.fishking.v2.controller.fishking;

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


}
