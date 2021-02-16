package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayTideDto {
    private Long observerId;
    private String observerName;
    private Boolean isAlerted;
    private String date;
//    private String tide;
    private String weather;
    private List<TidalLevelResponse> tideList;
//    private ArrayList<String> tideTimeList;
//    private ArrayList<String> tideLevelList;
    private Boolean highWater;
    private Boolean highWaterBefore1;
    private Boolean highWaterBefore2;
    private Boolean highWaterAfter1;
    private Boolean highWaterAfter2;
    private Boolean lowWater;
    private Boolean lowWaterBefore1;
    private Boolean lowWaterBefore2;
    private Boolean lowWaterAfter1;
    private Boolean lowWaterAfter2;

}
