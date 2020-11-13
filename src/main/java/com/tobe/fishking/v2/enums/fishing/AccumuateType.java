package com.tobe.fishking.v2.enums.fishing;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum AccumuateType /*implements IEnumModel*/ {

    use("사용"),
    accumulate("적립");

    private String value;
    AccumuateType(String value) {
        this.value = value;
    }

    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/


}