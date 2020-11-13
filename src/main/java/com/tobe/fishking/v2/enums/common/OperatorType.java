package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum OperatorType implements IEnumModel {

    or("or"),
    and("and");

    private String value;
    OperatorType(String value) {
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
