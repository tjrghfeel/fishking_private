package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddShipCamera {

    @ApiParam(value = "카메라 시리얼")
    private String serial;
    @ApiParam(value = "카메라 이름")
    private String name;

}
