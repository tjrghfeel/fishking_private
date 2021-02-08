package com.tobe.fishking.v2.model.fishing;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTideAlertDto {
    private Long observerId;
    private Integer[] tide;
    private Integer[] day;
    private Integer[] time;
}
