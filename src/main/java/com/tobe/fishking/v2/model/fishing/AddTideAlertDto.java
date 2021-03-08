package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTideAlertDto {
    private Long observerId;
    private ArrayList<String> tide;
    private ArrayList<String> day;
    private ArrayList<String> time;
}
