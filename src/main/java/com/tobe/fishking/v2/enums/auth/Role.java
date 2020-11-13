package com.tobe.fishking.v2.enums.auth;


//import com.tobe.fishking.v2.enums.IEnumModel;
import lombok.Getter;

@Getter
public enum Role  /*implements IEnumModel*/ {

    admin("관리자"),
    shipowner("선주"),
    member("회원"),
    guest("게스트");

    private String value;

    Role(String value) {
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