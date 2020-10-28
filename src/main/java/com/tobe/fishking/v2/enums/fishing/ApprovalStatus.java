package com.tobe.fishking.v2.enums.fishing;

public enum ApprovalStatus {
    ing("진행"),
    approved("확인(승인)"),
    fail("실패");

    private String value;

    ApprovalStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
