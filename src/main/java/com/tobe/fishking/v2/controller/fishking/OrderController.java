package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@RestController
@RequestMapping("/v2/api")
@Api(tags = {"예약 및 주문 (스마트출조)"})
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrdersService ordersService;

    @ApiOperation(value = "예약 탭 리스트", notes = "예약 리스트. " +
            "\n " +
            "\n")
    @GetMapping("/orders/{page}")
    public Page<OrderListResponse> searchOrders(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable Integer page,
                                                SearchOrdersDTO searchOrdersDTO) throws EmptyListException {
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.searchOrders(searchOrdersDTO, memberId, page);
    }

//    @GetMapping("/changeusername")
//    public void cccc() {
//        memberService.ttt();
//    }
}
