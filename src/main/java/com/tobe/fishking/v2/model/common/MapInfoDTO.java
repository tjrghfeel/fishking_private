package com.tobe.fishking.v2.model.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Builder(access = AccessLevel.PRIVATE)
@Getter
public class MapInfoDTO {

    /*조행기 ,조앻일지, 낙씨포이트(999) */
    @ApiModelProperty(value = "구분")
    private Long id;

    @ApiModelProperty(value = "위도")
    private Long latitude;

    @ApiModelProperty(value = "경도")
    private Long longitude;

    @ApiModelProperty(value = "타입")
    private int typee;

}
