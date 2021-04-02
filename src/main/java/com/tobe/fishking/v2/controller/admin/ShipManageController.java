package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.model.admin.ShipManageDtoForPage;
import com.tobe.fishking.v2.model.admin.ShipSearchConditionDto;
import com.tobe.fishking.v2.service.admin.ShipManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags={"선박 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class ShipManageController {
    private final ShipManageService shipManageService;

    //조건 검색
    @ApiOperation(value = "선박 검색")
    @GetMapping("/manage/ship/list/{page}")
    public Page<ShipManageDtoForPage> getShipList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page,
            ShipSearchConditionDto dto
    ){
        return shipManageService.getShipList(dto, token,page);
    }
}
