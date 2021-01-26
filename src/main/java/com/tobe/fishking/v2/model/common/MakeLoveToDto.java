package com.tobe.fishking.v2.model.common;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeLoveToDto {
    @NotNull
    private String takeType;
    @NotNull
    private Long linkId;
}
