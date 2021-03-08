package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTideLevelAlertDto {
//    @NotNull
//    private Integer tideHighLow;
//    private Integer time;
    private ArrayList<String> highTideAlert;
    private ArrayList<String> lowTideAlert;
    private Long observerId;
}
