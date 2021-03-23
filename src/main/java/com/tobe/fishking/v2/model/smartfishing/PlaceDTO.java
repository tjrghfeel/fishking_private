package com.tobe.fishking.v2.model.smartfishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDTO {

    @ApiParam(value = "위도")
    private @Valid Double latitude;
    @ApiParam(value = "경도")
    private @Valid Double longitude;
    @ApiParam(value = "갯바위 명칭")
    private @Valid String name;
    @ApiParam(value = "시/도")
    private @Valid String sido;
    @ApiParam(value = "시/군/구")
    private @Valid String sigungu;
    @ApiParam(value = "읍/면/동")
    private @Valid String dong;
    @ApiParam(value = "주소")
    private @Valid String address;
    @ApiParam(value = "평균수심")
    private @Valid Double averageDepth;
    @ApiParam(value = "저질")
    private @Valid String floorMaterial;
    @ApiParam(value = "적정물때")
    private @Valid String tideTime;
    @ApiParam(value = "소개")
    private @Valid String introduce;
    @ApiParam(value = "경도")
    private @Valid Boolean isOpen;
    @ApiParam(value = "포인트 리스트")
    private @Valid List<PlacePointDTO> points;

}
