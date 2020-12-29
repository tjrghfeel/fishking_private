package com.tobe.fishking.v2.enums.common;


import com.tobe.fishking.v2.enums.IEnumModel;

public enum SortType  implements IEnumModel {
    createDate("최신순"),
    readCount("조회순"),
    popular("인기순"),
    distance("거리순"),
    lowPrice("낮은가격순"),
    highPrice("높은가격순"),
    review("리뷰순"),
    sell("판매순"),
    name("명칭순");


    private String value;
    SortType(String value) {
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
