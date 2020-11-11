package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum SearchPublish implements IEnumModel {

    TOTAL("통합"),
    COMPANY("업체"),
    TV("어복TV"),
    FISHINGDIARY("조황일지"),
    FISHINGDIARY2("조행기");

    private String value;
    SearchPublish(String value) {
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