package com.tobe.fishking.v2.enums.fishing;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum ApprovalStatus /*implements IEnumModel*/ {
    ing("진행"),
    approved("확인(승인)"),
    fail("실패");

    private String value;

    ApprovalStatus(String value) {
        this.value = value;
    }

   /* @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/

}
