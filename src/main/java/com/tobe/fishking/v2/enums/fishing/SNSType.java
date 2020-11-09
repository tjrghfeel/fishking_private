package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum SNSType implements IEnumModel {

    kakao("카카오톡"),
    navaer("네이버"),
    facebook("페이스북");


    private String value;

    SNSType(String value) {
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
