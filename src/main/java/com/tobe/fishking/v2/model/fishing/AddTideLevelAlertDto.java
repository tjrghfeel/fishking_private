package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTideLevelAlertDto {
    @NotNull
    private Integer tideHighLow;
    private Integer time;
    private Long observerId;
}
