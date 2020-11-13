package com.tobe.fishking.v2.enums.fishing;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum FishSpecies /*implements IEnumModel*/ {



/*

    고등어 - mackerel
    갈치 - hairtail




    삼치 - japanese spanish mackerel
    아구 - monkfish
    오징어/갑오징어 - squid/cuttlefish
    가재미 - flounder
    게 - crab
    광어 - fluke
    굴 - oyster
    꽁치 - pike/saury
    날치 - flying fish
    다랑어 - tuna
    대구 - codfish
    도미 - porgy
    멸치 - anchovy
    명태/생태 - whiting
    문어 - octopus
    미꾸라지 - butter fish/mud fish
    바닷가재 - lobster
    방어 - yellow tail
    장어 - eel
    빙어 - smelt
    새우 - shrimp
    성게 - sea urchin
    송어 - trout
    숭어 - mullet
    연어 - salmon
    잉어 - carp
    적도미/참돔 - red snapper
    전복 - abalone
    조개 - clam
    청어 - herring
    홍합 - mussel
    새우 - shrimp
    메기 - catfish
    농어 - bass
    민물가재 - crawfish
    조기 - croaker
    민어- kingfish
    우럭 - rock cod
    정어리 - sardine
    한치 - calamari
    멍게 - Sea squirt/sea pineapple
*/



    mackerel("고등어"),
    hairtail("갈치");


    private String value;

    FishSpecies(String value) {
        this.value = value;
    }


    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/


}
