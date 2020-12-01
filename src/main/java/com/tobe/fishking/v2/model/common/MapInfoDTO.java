package com.tobe.fishking.v2.model.common;


import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Builder(access = AccessLevel.PRIVATE)
@Getter
public class MapInfoDTO {

    @ApiModelProperty(value = "구분")
    private String searchKeyWord;

    @ApiModelProperty(value = "위도")
    private String oLAT;

    @ApiModelProperty(value = "경도")
    private String oLON;


}
