package com.tobe.fishking.v2.enums.fishing;

public enum SNSType {

    kakao("카카오톡"),
    navaer("네이버"),
    facebook("페이스북");


    private String value;

    SNSType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
