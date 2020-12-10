package com.tobe.fishking.v2.enums.auth;


//import com.tobe.fishking.v2.enums.IEnumModel;
import com.tobe.fishking.v2.enums.IEnumModel;
import lombok.Getter;

@Getter
public enum Gender implements IEnumModel {

    boy("남"),
    girl("여");

    private String value;

    Gender(String value) {
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