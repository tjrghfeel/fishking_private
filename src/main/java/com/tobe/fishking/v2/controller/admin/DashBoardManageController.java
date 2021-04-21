package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.DashBoardManageDto;
import com.tobe.fishking.v2.service.admin.DashBoardManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"대시보드"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class DashBoardManageController {
    private final DashBoardManageService dashBoardManageService;

    @ApiOperation(value = "대시보드 데이터 조회")
    @GetMapping("/manage/dashboard/main")
    public DashBoardManageDto getDashBoard(
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        return dashBoardManageService.getDashBoard(token);
    }

}
