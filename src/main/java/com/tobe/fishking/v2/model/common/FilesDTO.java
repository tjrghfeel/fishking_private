package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.enums.fishing.Meridiem;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class FilesDTO {

    @ApiModelProperty(value = "다운로드URL")
    private String downloadUrl;

}
