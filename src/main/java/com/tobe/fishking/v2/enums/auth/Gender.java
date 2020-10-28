package com.tobe.fishking.v2.enums.auth;


import lombok.Getter;

@Getter
public enum Gender {

    boy("남"),
    girl("여");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}