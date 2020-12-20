package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FishingTechnic implements IEnumModel {

    DOWNSHOT("다운샷"),
    LURE("루어"),
    MAC("맥낚시"),
    LEASH("목줄찌"),
    ROD("민장대"),
    SIGLELINE("외줄"),
    ONETWO("원투"),
    POLE("장대"),
    JIGGING("지깅"),
    BOBBER("찌"),
    CARGO("카고"),
    HOOT("훌치기");

    private String value;

    FishingTechnic(String value) {
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
