package com.tobe.fishking.v2.enums.fishing;

public enum PayMethod {

    cash("현금"),
    card("카드"),
    simple("간편결제");

    private String value;

    PayMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
