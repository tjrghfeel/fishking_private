package com.tobe.fishking.v2.enums.fishing;

public enum OrderStatus {

    waite(" 접수대기"),
    receipt("접수"),
    payment("결제");

    private String value;
    OrderStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}