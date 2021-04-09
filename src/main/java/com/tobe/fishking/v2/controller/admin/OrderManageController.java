package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.model.admin.OrderManageDtoForPage;
import com.tobe.fishking.v2.model.admin.OrderSearchConditionDto;
import com.tobe.fishking.v2.service.admin.OrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags={"예약 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class OrderManageController {
    private final OrderManageService orderManageService;

    @ApiOperation(value="예약 리스트 검색")
    @GetMapping("/manage/order/list/{page}")
    public Page<OrderManageDtoForPage> getOrderList(
            @RequestHeader("Authorization") String token,
            OrderSearchConditionDto dto,
            @PathVariable("page") int page
    ){
        return orderManageService.getOrderList(dto, token, page);
    }
}
