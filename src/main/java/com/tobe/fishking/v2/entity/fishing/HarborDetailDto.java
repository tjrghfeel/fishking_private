package com.tobe.fishking.v2.entity.fishing;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HarborDetailDto {
    private Long id;
    private String name;
    private String sido;
    private String gungu;
    private String address;
    private Double lat;
    private Double lon;
    private String observerCode;
    private Long observerId;

    private String adtId;
    private String adtPw;

}
