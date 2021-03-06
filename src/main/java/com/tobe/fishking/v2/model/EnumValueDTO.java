package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.enums.IEnumModel;


public class EnumValueDTO {
    private String key;
    private String value;

    public EnumValueDTO(IEnumModel enumModel) {
        key = enumModel.getKey();
        value = enumModel.getValue();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}