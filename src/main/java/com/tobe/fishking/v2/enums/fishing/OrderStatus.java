package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum OrderStatus implements IEnumModel {

    waite(" 접수대기"),
    receipt("접수"),
    payment("결제");

    private String value;
    OrderStatus(String value) {
        this.value = value;
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