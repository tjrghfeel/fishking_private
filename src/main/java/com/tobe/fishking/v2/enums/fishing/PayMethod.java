package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum PayMethod  implements IEnumModel {

    CARD("신용카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    ACCOUNT("계좌이체"),
    PHONE("핸드폰결제");

    private String value;

    PayMethod(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }
    public String getValue() {
        return this.value;
    }

}
