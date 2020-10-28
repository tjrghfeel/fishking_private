package com.tobe.fishking.v2.enums.common;

public enum CouponType {

    amount("정액"),
    rate("정률");

    private String value;

    CouponType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
