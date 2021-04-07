package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class SearchOrdersDTO {

    @ApiParam(value = "검색 시작일", example = "2021-01-01")
    public @Valid String startDate = "";
    @ApiParam(value = "검색 종료일", example = "2021-06-01")
    public @Valid String endDate = "";
    @ApiParam(value = "주문상태, 예약 진행중: bookRunning, 대기자예약: waitBook, 예약완료: bookConfirm, 예약확정: bookFix, 출조완료: fishingComplete, 예약 취소: bookCancel")
    public @Valid String status = "";
    @ApiParam(value = "결제방법, 신용카드: CARD, 가상계좌: VIRTUAL_ACCOUNT, 계좌이체: ACCOUNT, 핸드폰결제: PHONE")
    public @Valid String payMethod = "";
    @ApiParam(value = "검색어 구분. 예약자명: username, 연락처: phone, 선박명: shipName, 상품명: goodsName")
    public @Valid String keywordType = "username";
    @ApiParam(value = "검색어")
    public @Valid String keyword = "";

}
