package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 메인"})
@RequiredArgsConstructor
public class SmartFishingMainController {

    private final MemberService memberService;

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
    @GetMapping("/smartfishing/dashboard")
    public Map<String, Object> dashboard(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Map<String, Object> result = new HashMap<>();
        return result;
    }

}
