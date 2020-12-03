package com.tobe.fishking.v2.enums.fishing;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum Meridiem /*implements IEnumModel*/ {

    am("오전"),
    pm("오후");

    private String value;
    Meridiem(String value) {
        this.value = value;
    }
    public String getValue(){return this.value;}

    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/

}