package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ReserveDTO {

    @ApiParam(value = "상품 id")
    private @Valid Long goodsId;

    @ApiParam(value = "사용한 쿠폰 id", defaultValue = "0")
    private @Valid Long couponId;

    @ApiParam(value = "총 가격")
    private @Valid Long totalPrice;

    @ApiParam(value = "할인 가격")
    private @Valid Long discountPrice;

    @ApiParam(value = "쿠폰 적용 후 가격")
    private @Valid Long paymentPrice;

    @ApiParam(value = "예약한 조업일 yyyy-MM-dd")
    private @Valid String date;

    @ApiParam(value = "예약자 이름")
    private @Valid String reservePersonName;

    @ApiParam(value = "예약자 전화번호")
    private @Valid String reservePersonPhone;

    @ApiParam(value = "승선자 수")
    private @Valid Integer personCount;

    @ApiParam(value = "승선자 승선위치 리스트")
    private @Valid List<Integer> positions;

    @ApiParam(value = "승선자 생년월일 리스트")
    private @Valid String[] personsBirthdate;

    @ApiParam(value = "승선자 전화번호 리스트")
    private @Valid String[] personsPhone;

    @ApiParam(value = "승선자 비상연락처 리스트")
    private @Valid String[] emergencyPhone;

    @ApiParam(value = "승선자 이름 리스트")
    private @Valid String[] personsName;

    @ApiParam(value = "결제수단. 신용카드: '1000000000', 가상계좌: '0100000000', 계좌이체: '0010000000', 휴대폰결제: '0000010000'")
    private @Valid String payMethod;
}
