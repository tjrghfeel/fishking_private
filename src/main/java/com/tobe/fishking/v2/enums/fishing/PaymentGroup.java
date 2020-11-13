package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

import java.util.Arrays;

public enum PaymentGroup implements IEnumModel {

    CASH("현금", new PaymentOption[]{
            PaymentOption.BANK_TRANSFER, PaymentOption.DEPOSITLESS, PaymentOption.FIELD_PAYMENT, PaymentOption.TOSS
    }),
    PG("결제대행사", new PaymentOption[]{
            PaymentOption.MOBILE, PaymentOption.CREDIT_CARD, PaymentOption.SIMPLE_PAY
    }),
    ETC("기타", new PaymentOption[]{
            PaymentOption.POINT, PaymentOption.COUPON
    }),
    EMPTY("없음", new PaymentOption[]{});

    private String value;
    private PaymentOption[] containPayment;

    PaymentGroup(String value, PaymentOption[] containPayment) {
        this.value = value;
        this.containPayment = containPayment;
    }

    public static PaymentGroup findGroup(PaymentOption searchTarget){
        return Arrays.stream(PaymentGroup.values())
                .filter(group -> hasPaymentOption(group, searchTarget))
                .findAny()
                .orElse(PaymentGroup.EMPTY);
    }

    private static boolean hasPaymentOption(PaymentGroup from, PaymentOption searchTarget){
        return Arrays.stream(from.containPayment)
                .anyMatch(containPay -> containPay == searchTarget);
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