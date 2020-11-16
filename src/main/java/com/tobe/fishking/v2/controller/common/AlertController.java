package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.service.common.AlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"알람"})
@RequestMapping(value = "/v1/api")
public class AlertController {

    @Autowired
    AlertService alertService;

    /*알람 추가. */
    @ApiOperation(value = "알람 추가")
    @PostMapping("/alert")
    public Long addAlert(Long memberId, Long[] commonCodeIdList) throws ResourceNotFoundException {
        return alertService.addAlert(memberId,commonCodeIdList);
    }

    /**/

}
