package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShipSearchDTO {

    @ApiParam(value = "리스트 타입. 선상: 'ship', 갯바위: 'seaRocks'", required = true, example = "ship")
    private @Valid String fishingType = "";

    @ApiParam(value = "정렬순서. 인기순: 'popular', 거리순: 'distance', 낮은가격순: 'lowPrice', 높은가격순: 'highPrice', 리뷰순: 'review', 판매순: 'sold'", example = "lowPrice")
    private @Valid String orderBy = "";

    @ApiParam(value = "페이지. 0부터 시작", example = "0")
    private @Valid Integer pageNumber = 1;

    @ApiParam(value = "페이지 당 데이터 수.", example = "5")
    private @Valid Integer size = 5;

    @ApiParam(value = "날짜 검색. 하루 선택 yyyy-MM-dd", example = "2021-01-26")
    private @Valid String fishingDate = "";

    @ApiParam(value = "어종검색 리스트", example = "[고등어, 우럭]")
    private @Valid List<String> species = new ArrayList<>();

    @ApiParam(value = "옵션-서비스제공 리스트", example = "[조식, 사진촬영]")
    private @Valid List<String> services = new ArrayList<>();

    @ApiParam(value = "옵션-편의시설 리스트", example = "[wifi, 스낵, 화장실]")
    private @Valid List<String> facilities = new ArrayList<>();

    @ApiParam(value = "옵션-장르 리스트", example = "[다운샷]")
    private @Valid List<String> genres = new ArrayList<>();

    @ApiParam(value = "지역검색. 시도 범위", example = "인천광역시")
    private @Valid String sido = "";

    @ApiParam(value = "실시간영상 유무 빈값의 경우 전부", example = "false")
    private @Valid Boolean hasRealTimeVideo = null;

}
