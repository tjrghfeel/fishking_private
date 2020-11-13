package com.tobe.fishking.v2.enums.board;


//import com.tobe.fishking.v2.enums.IEnumModel;
import lombok.Getter;

@Getter
public enum ReturnType /*implements IEnumModel*/ {

    email("이메일"),
    tel("연락처");

    private String value;

    ReturnType(String value) {
        this.value = value;
    }

    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }*/


}