package com.tobe.fishking.v2.enums.common;

public enum BannerType {

    images("배너이미지");


    private String value;

    BannerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
