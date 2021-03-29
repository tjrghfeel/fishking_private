package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum ChannelType  implements IEnumModel {

    important("중요"),
    general("일반");

    private String value;
    ChannelType(String value) {
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
