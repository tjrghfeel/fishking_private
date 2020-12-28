package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

import javax.swing.plaf.synth.Region;
import java.util.Arrays;

public enum RegionGroup implements IEnumModel {

    CAPITAL("수도권", new RegionMiddle[]{
            RegionMiddle.Jung_gu, RegionMiddle.Siheung, RegionMiddle.Hwaseong, RegionMiddle.Ongjin, RegionMiddle.Pyeongtaek
    }),
    CHUNGNAM("충청남도", new RegionMiddle[]{
            RegionMiddle.Dangjin, RegionMiddle.Taean, RegionMiddle.Boryeong, RegionMiddle.Seocheon
    }),
    JEONBUK("전라북도", new RegionMiddle[]{
            RegionMiddle.Buan, RegionMiddle.Gunsan
    }),
    JEONNAM("전라남도", new RegionMiddle[]{
            RegionMiddle.Mokpo
    }),

    EMPTY("없음", new RegionMiddle[]{});


    private String value;
    private RegionMiddle[] containPayment;

    RegionGroup(String value, RegionMiddle[] containPayment) {
        this.value = value;
        this.containPayment = containPayment;
    }

    public static RegionGroup findGroup(RegionMiddle searchTarget){
        return Arrays.stream(RegionGroup.values())
                .filter(group -> hasRegionMiddle(group, searchTarget))
                .findAny()
                .orElse(RegionGroup.EMPTY);
    }

    private static boolean hasRegionMiddle(RegionGroup from, RegionMiddle searchTarget){
        return Arrays.stream(from.containPayment)
                .anyMatch(containPay -> containPay == searchTarget);
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