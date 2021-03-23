package com.tobe.fishking.v2.model.smartfishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class PlacePointDTO {

    @ApiParam(value = "위도")
    private @Valid Double latitude;
    @ApiParam(value = "경도")
    private @Valid Double longitude;

}
