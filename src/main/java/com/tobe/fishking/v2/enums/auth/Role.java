package com.tobe.fishking.v2.enums.auth;


import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ROLE_ADMIN"),
    SHIPOWNER("ROLE_SHIPOWNER"),
    MEMBER("ROLE_MEMBER"),
    GUEST("ROLE_GUEST");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}