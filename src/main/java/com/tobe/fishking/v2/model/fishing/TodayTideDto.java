package com.tobe.fishking.v2.model.fishing;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayTideDto {
    private Long observerId;
    private String observerName;
    private Boolean isAlerted;
    private String todayDate;
    private String weather;
    private String[] tideTimeList;
    private String[] tideLevelList;
    private String[] alertKeyList;
}
