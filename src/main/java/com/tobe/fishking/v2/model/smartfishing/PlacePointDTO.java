package com.tobe.fishking.v2.model.smartfishing;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class PlacePointDTO {

    @ApiModelProperty(example = "위도")
    private @Valid Double latitude;
    @ApiModelProperty(example = "경도")
    private @Valid Double longitude;

}
