package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum PaymentOption  implements IEnumModel {
    MOBILE("모바일"),
    CREDIT_CARD("신용카드"),
    SIMPLE_PAY("간편결제"),
    BANK_TRANSFER("계좌이체"),
    DEPOSITLESS("무통장입금"),
    FIELD_PAYMENT("현장결제"),
    TOSS("토스"),
    POINT("포인트"),
    COUPON("쿠폰");

    private String value;

    PaymentOption(String value) {
        this.value = value;
    }

    public String getViewName() {
        return value;
    }


    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }



}