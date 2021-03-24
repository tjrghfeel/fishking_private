package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 정산"})
@RequiredArgsConstructor
public class PaidController {

    private final MemberService memberService;
    private final OrdersService ordersService;

    @GetMapping("/calculate")
    @ApiOperation(value = "정산 리스트", notes = "정산 탭의 리스트. " +
            "\n 검색어 타입은 선박명 하나 입니다." +
            "\n 검색 시 년, 월 검색이 필요합니다. 기본값은 현재 년, 월 입니다. " +
            "\n 아래는 검색 요청 파라미터입니다" +
            "\n year: 연 선택 (2021)" +
            "\n month: 달 선택 (01, 02, ... , 12)" +
            "\n shipName: 선박명 검색값" +
            "\n isCalculate: 정산상태 검색값 (완료: true, 대기: false, 전체: null)" +
            "\n 아래는 응답 입니다." +
            "\n {" +
            "\n year: 검색 년도" +
            "\n month: 검색 달" +
            "\n content: [{" +
            "\n     shipId: 상품 id" +
            "\n     shipName: 선박명" +
            "\n     total: 정산금액" +
            "\n     order: 예약금액" +
            "\n     cancel: 취소금액" +
            "\n }, ... ]" +
            "\n }" +
            "\n ")
    public Map<String, Object> searchCalculate(@RequestHeader(name = "Authorization") String token,
                                               @RequestParam String year,
                                               @RequestParam String month,
                                               @RequestParam(defaultValue = "") String shipName,
                                               @RequestParam(defaultValue = "null") Boolean isCalculate) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.searchCalculation(memberId, shipName, year, month, isCalculate);
    }

    @GetMapping("/calculate/now")
    @ApiOperation(value = "정산 예정 금액 리스트", notes = "정산 예정 금액 리스트" +
            "\n 항상 현재의 년/월 로 검색한 결과를 보냅니다" +
            "\n year: 연 선택 (2021)" +
            "\n month: 달 선택 (01, 02, ... , 12)" +
            "\n shipName: 선박명 검색값" +
            "\n isCalculate: 정산상태 검색값 (완료: true, 대기: false, 전체: null)" +
            "\n 아래는 응답 입니다." +
            "\n {" +
            "\n year: 검색 년도" +
            "\n month: 검색 달" +
            "\n content: [{" +
            "\n     shipId: 상품 id" +
            "\n     shipName: 선박명" +
            "\n     total: 정산금액" +
            "\n     order: 예약금액" +
            "\n     cancel: 취소금액" +
            "\n }, ... ]" +
            "\n }" +
            "\n ")
    public Map<String, Object> getNowCalculate(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.getCalculationThisMonth(memberId);
    }

}
