package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum TakeType  implements IEnumModel {

    goods("상품"),
    ship("업체"),
    fishkingDaily("조황일지"),
    fishingBlog("조행기"),
    comment("댓글"),
    fishkingTv("어복TV");

    private String value;
    TakeType(String value) {
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
