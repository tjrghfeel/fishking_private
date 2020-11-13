package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum AlertType /*implements IEnumModel*/ {

    mmmber("사용자");

    private String value;
    AlertType(String value) {
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
