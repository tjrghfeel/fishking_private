package com.tobe.fishking.v2.enums.common;

public enum TakeType {

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
    public String getValue() {
        return this.value;
    }

}
