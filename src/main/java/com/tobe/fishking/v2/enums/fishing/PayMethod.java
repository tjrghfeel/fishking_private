package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum PayMethod  implements IEnumModel {

    cash("현금"),
    card("카드"),
    simple("간편결제");

    private String value;

    PayMethod(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }
    public String getValue() {
        return this.value;
    }

}
