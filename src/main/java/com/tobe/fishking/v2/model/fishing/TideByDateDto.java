package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import java.util.ArrayList;

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
//    private String weather;
    private ArrayList<String> tideTimeList;
    private ArrayList<String> tideLevelList;
    private Boolean[] alertTideList;
    private Boolean[] alertDayList;
    private Boolean[] alertTimeList;

}
