package com.tobe.fishking.v2.model.smartfishing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "갯바위 등록")
public class PlaceDTO {

    @ApiModelProperty(example = "위도. 필수값", required = true)
    private @Valid Double latitude;
    @ApiModelProperty(example = "경도. 필수값", required = true)
    private @Valid Double longitude;
    @ApiModelProperty(example = "갯바위 명칭. 필수값", required = true)
    private @Valid String name;
    @ApiModelProperty(example = "시/도. 필수값", required = true)
    private @Valid String sido;
    @ApiModelProperty(example = "시/군/구. 필수값", required = true)
    private @Valid String sigungu;
    @ApiModelProperty(example = "읍/면/동")
    private @Valid String dong = "";
    @ApiModelProperty(example = "주소. 필수값", required = true)
    private @Valid String address;
    @ApiModelProperty(example = "평균수심")
    private @Valid Double averageDepth;
    @ApiModelProperty(example = "저질")
    private @Valid String floorMaterial;
    @ApiModelProperty(example = "적정물때")
    private @Valid String tideTime;
    @ApiModelProperty(example = "소개")
    private @Valid String introduce;
    @ApiModelProperty(example = "공개여부 공개: true, 비공개: false. 필수값", required = true)
    private @Valid Boolean isOpen;
//    @ApiModelProperty(example = "포인트 리스트")
    private @Valid List<PlacePointDTO> points;

}
