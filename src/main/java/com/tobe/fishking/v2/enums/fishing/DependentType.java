package com.tobe.fishking.v2.enums.fishing;

public enum DependentType {

    board("게시판"),
    fishingDaily("조황일지"),
    fishingBlog("조황기");

    private String value;
    DependentType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}