package com.tobe.fishking.v2.model.fishing;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AddGoods {

    @ApiParam(value = "선박 id")
    private @Valid Long shipId;
    @ApiParam(value = "상품명")
    private @Valid String name;
    @ApiParam(value = "가격")
    private @Valid Integer amount;
    @ApiParam(value = "최소 인원")
    private @Valid Integer minPersonnel;
    @ApiParam(value = "최대 인원")
    private @Valid Integer maxPersonnel;
    @ApiParam(value = "운항 시작시간. 09:00")
    private @Valid String fishingStartTime;
    @ApiParam(value = "운항 종료시간. 17:00")
    private @Valid String fishingEndTime;
    @ApiParam(value = "상태, 판매:true, 대기: false")
    private @Valid Boolean isUse;
    @ApiParam(value = "어종 리스트. 어종의 code 를 보내주세요", name = "species[]")
    private List<String> species;
//    private String[] species;
    @ApiParam(value = "조업일 리스트", name = "fishingDates[]")
    private List<String> fishingDates;
//    private String[] fishingDates;
    @ApiParam(value = "예약 확정 방법. 자동확정: 'auto', 선장 수동 확정: 'approval'")
    private @Valid String reserveType;
    @ApiParam(value = "예약시 위치 선정 여부")
    private @Valid Boolean positionSelect;
    @ApiParam(value = "추가운행여부")
    private @Valid Boolean extraRun;
    @ApiParam(value = "추가 운행 최소 인원 수 ")
    private @Valid Integer extraPersonnel;
    @ApiParam(value = "최대 선박 수 ")
    private @Valid Integer extraShipNumber;

}
