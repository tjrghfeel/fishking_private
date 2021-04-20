package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FishingTechnic implements IEnumModel {

    BOBBER("찌낚시"),
    LURE("루어낚시"),
    SIGLELINE("외줄낚시"),
    ONETWO("원투낚시"),
    ROD("민장대");
//    DOWNSHOT("다운샷"),
//    MAC("맥낚시"),
//    LEASH("목줄찌"),
//    POLE("장대"),
//    JIGGING("지깅"),
//    CARGO("카고"),
//    HOOT("훌치기");

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
