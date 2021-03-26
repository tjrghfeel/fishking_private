package com.tobe.fishking.v2.model.smartsail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class CameraUpdateDTO {

    private @Valid Long cameraId;
    private @Valid Boolean isUse;

}
