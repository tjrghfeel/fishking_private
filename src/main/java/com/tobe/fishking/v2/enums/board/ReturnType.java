package com.tobe.fishking.v2.enums.board;


import lombok.Getter;

@Getter
public enum ReturnType {

    email("이메일"),
    tel("연락처");

    private String value;

    ReturnType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}