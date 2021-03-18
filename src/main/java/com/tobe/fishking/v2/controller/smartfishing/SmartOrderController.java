package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
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

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/v2/api")
@Api(tags = {"예약 및 주문 (스마트출조)"})
@RequiredArgsConstructor
public class SmartOrderController {

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
            "\n }" +
            "\n 상태가 '예약진행중' 인 경우 예약승인 버튼 노출")
    @GetMapping("/orders/{page}")
    public Page<OrderListResponse> searchOrders(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable Integer page,
                                                SearchOrdersDTO searchOrdersDTO) throws EmptyListException, ResourceNotFoundException, NotAuthException {
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
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
            "\n     birthday: 생년월일" +
            "\n     phone: 전화번호" +
            "\n     name: 이름" +
            "\n     emergencyPhone: 비상연락처" +
            "\n }, ... ]" +
            "\n shipPositions: 선박의 사용 위치" +
            "\n reservePositions: 예약에서 지정한 위치" +
            "\n shipType: 선박 타입 (3: 8인승, 5: 18인승, 9: 22인승)" +
            "\n cancelDate: 취소시 취소 날짜. 취소 아닌경우 빈 문자열" +
            "\n refundAmount: 취소금액. 취소 아닌경우 null" +
            "\n 예약자 생년월일은 빼주세요")
    @GetMapping("/orders/detail/{orderId}")
    public OrderDetailResponse getOrderDetail(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable Long orderId) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        return ordersService.getOrderDetail(orderId);
    }

    @ApiOperation(value = "예약 승인", notes = "예약 승인 {" +
            "\n success: 성공여부" +
            "\n message: 메세지" +
            "\n }")
    @PostMapping("/order/confirm")
    public Map<String, Object> confirmOrder(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam Long orderId) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Map<String, Object> result = new HashMap<>();
        if (ordersService.confirmOrder(orderId)) {
            result.put("success", true);
            result.put("message", "승인되었습니다.");
        } else {
            result.put("success", false);
            result.put("message", "실패했습니다. 다시 시도해주세요.");
        }
        return result;
    }

    @ApiOperation(value = "예약 승인", notes = "예약 승인 {" +
            "\n success: 성공여부" +
            "\n message: 메세지" +
            "\n }")
    @PostMapping("/order/cancel")
    public Map<String, Object> cancelOrder(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam Long orderId) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> result = new HashMap<>();
        if (ordersService.cancelOrder(orderId, member)) {
            result.put("success", true);
            result.put("message", "승인되었습니다.");
        } else {
            result.put("success", false);
            result.put("message", "실패했습니다. 다시 시도해주세요.");
        }
        return result;
    }

//    @GetMapping("/changeusername")
//    public void cccc() {
//        memberService.ttt();
//    }
}
