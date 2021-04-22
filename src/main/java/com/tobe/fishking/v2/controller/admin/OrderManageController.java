package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.OrderManageDtoForPage;
import com.tobe.fishking.v2.model.admin.OrderSearchConditionDto;
import com.tobe.fishking.v2.service.admin.OrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            @Valid OrderSearchConditionDto dto,
            @PathVariable("page") int page
    ) throws ServiceLogicException {
        try{
            if(dto.getPayMethod()!=null){PayMethod.valueOf(dto.getPayMethod());}
            if(dto.getOrderStatus() != null) { OrderStatus.valueOf(dto.getOrderStatus());}
        }
        catch(Exception e){throw new ServiceLogicException("payMethod 또는 orderStatus의 값이 잘못되었습니다.");}

        return orderManageService.getOrderList(dto, token, page);
    }
}
