package com.tobe.fishking.v2.enums.common;

public enum PostTitle {

    fishingDaily("조황일지"),
    fishingBlog("조행기");

    private String value;

    PostTitle(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
