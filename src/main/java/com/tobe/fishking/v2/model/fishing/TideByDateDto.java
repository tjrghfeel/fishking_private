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
public class TideByDateDto {
    private Long observerId;
    private String observerName;
    private Boolean isAlerted;
    private String date;
//    private String tide;
    private ArrayList<String> weather;
    private List<TidalLevelResponse> tideList;
//    private ArrayList<String> tideTimeList;
//    private ArrayList<String> tideLevelList;
    private ArrayList<String> alertTideList;
    private ArrayList<String> alertDayList;
    private ArrayList<String> alertTimeList;

}
