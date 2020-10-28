package com.tobe.fishking.v2.enums.fishing;

public enum FishingTechnic {

    downshot("다운샷"),
    lure("루어"),
    mac("맥낚시"),
    leash("목줄찌"),
    rod("민장대"),
    sigleline("외줄"),
    onetwo("원투"),
    pole("장대"),
    jigging("지깅"),
    bobber("찌"),
    cargo("카고"),
    hoot("훌치기");

    private String value;
    FishingTechnic(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }


}
