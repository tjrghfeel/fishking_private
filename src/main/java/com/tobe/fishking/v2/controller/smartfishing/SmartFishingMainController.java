package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.response.FishingDashboardResponse;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 메인"})
@RequiredArgsConstructor
public class SmartFishingMainController {

    private final MemberService memberService;
    private final OrdersService ordersService;

    @ApiOperation(value = "승인필요 리스트", notes = "대시보드 탭의 승인필요 리스트. " +
            "\n [ {" +
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
            "\n 상태가 '예약진행중' 인 경우 예약승인 버튼 노출")
    @GetMapping("/smartfishing/dashboard/bookRunning")
    public List<OrderListResponse> dashboardRunning(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.getBookRunning(memberId);
    }

    @ApiOperation(value = "예약완료 리스트", notes = "대시보드 탭의 예약완료 리스트. " +
            "\n [ {" +
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
            "\n 상태가 '예약진행중' 인 경우 예약승인 버튼 노출")
    @GetMapping("/smartfishing/dashboard/bookConfirm")
    public List<OrderListResponse> dashboardConfirm(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.getBookConfirm(memberId);
    }

    @ApiOperation(value = "결제현황", notes = "결제현황. {" +
            "\n countRunning: 승인필요 수" +
            "\n countConfirm: 예약완료 수" +
            "\n percentRunning: 승인필요 %" +
            "\n percentConfirm: 예약완료 %" +
            "\n countWait: 대기예약 수" +
            "\n countFix: 예약완료 수" +
            "\n percentWait: 대기예약 %" +
            "\n percentFix: 예약완료 %" +
            "\n countCancel: 총 데이터 수" +
            "\n countComplete: 총 페이지 수" +
            "\n percentCancel: 마지막페이지 여부" +
            "\n percentComplete: 첫페이지 여부" +
            "\n }" +
            "\n 상태가 '예약진행중' 인 경우 예약승인 버튼 노출")
    @GetMapping("/smartfishing/dashboard/count")
    public FishingDashboardResponse dashboardCount(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return ordersService.getStatus(memberId);
    }
}
