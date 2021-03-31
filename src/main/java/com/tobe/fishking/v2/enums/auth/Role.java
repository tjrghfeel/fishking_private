package com.tobe.fishking.v2.enums.auth;


//import com.tobe.fishking.v2.enums.IEnumModel;
import com.tobe.fishking.v2.enums.IEnumModel;
import lombok.Getter;

@Getter
public enum Role  implements IEnumModel {

    admin("관리자"),
    shipowner("업주"),
    member("회원"),
    guest("게스트"),//3
    marine("해경");//4

    private String value;

    Role(String value) {
        this.value = value;
    }


    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }


}