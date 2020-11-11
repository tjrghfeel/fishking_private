package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum AdType implements IEnumModel {

    premium("프리미엄"),
    recommend("추천");

    private String value;
    AdType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

}
