package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.OrderDetailResponse;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v2/api")
@Api(tags = {"예약 및 주문 (스마트출조)"})
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrdersService ordersService;

    @ApiOperation(value = "예약 탭 리스트", notes = "예약 리스트. {" +
            "\n content: [ {" +
            "\n     id: 예약 id" +
            "\n     shipName: 선박명" +
            "\n     goodsName: 상품명 (퍼블상 선박 명 뒤 | 뒤에 있는 갈치, 참돔 이 자리에 넣어주세요)" +
            "\n     orderNumber: 예약번호" +
            "\n     fishingDate: 출조일" +
            "\n     orderDate: 결제일" +
            "\n     reservePersonnel: 예약 인원" +
            "\n     totalAmount: 총 가격" +
            "\n     status: 예약상태" +
            "\n     profileImage: 예약자 프로필사진" +
            "\n     username: 예약자명" +
            "\n }, ... ]" +
            "\n totalElements: 총 데이터 수" +
            "\n totalPages: 총 페이지 수" +
            "\n last: 마지막페이지 여부" +
            "\n first: 첫페이지 여부" +
            "\n")
    @GetMapping("/orders/{page}")
    public Page<OrderListResponse> searchOrders(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable Integer page,
                                                SearchOrdersDTO searchOrdersDTO) throws EmptyListException {
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.searchOrders(searchOrdersDTO, memberId, page);
    }

    @ApiOperation(value = "예약 상세", notes = "예약 상세정보. " +
            "\n id: 예약 id" +
            "\n name: 상품명" +
            "\n fishingStartTime: 출항시간" +
            "\n amount: 상품가격" +
            "\n orderNumber: 예약번호(주문번호)" +
            "\n status: 예약상태" +
            "\n reserveName: 예약자명" +
            "\n reservePhone: 예약자 전화번호" +
            "\n rideList: 승선자 리스트 [ {" +
            "\n rideList: 승선자 리스트 [ {" +
            "\n     birthday: 생년월일" +
            "\n     phone: 전화번호" +
            "\n     name: 이름" +
            "\n     emergencyPhone: 비상연락처" +
            "\n }, ... ]" +
            "\n shipPositions:" +
            "\n reservePositions:" +
            "\n shipType:" +
            "\n cancelDate:" +
            "\n refundAmount:" +
            "\n")
    @GetMapping("/orders/detail/{orderId}")
    public OrderDetailResponse getOrderDetail(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable Long orderId) {
        return ordersService.getOrderDetail(orderId);
    }

//    @GetMapping("/changeusername")
//    public void cccc() {
//        memberService.ttt();
//    }
}
