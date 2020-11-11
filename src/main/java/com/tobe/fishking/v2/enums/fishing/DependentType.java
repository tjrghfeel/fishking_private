package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum DependentType implements IEnumModel {

    board("게시판"),
    fishingDaily("조황일지"),
    fishingBlog("조황기");

    private String value;
    DependentType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

}