package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum  PostTitle  implements IEnumModel {

    fishingDiary("조황일지"),
    fishingBlog("조행기");

    private String value;

      PostTitle(String value) {
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
