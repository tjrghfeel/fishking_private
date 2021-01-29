package com.tobe.fishking.v2.model.board;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAccuseDto {
    @NotNull
    private Long linkId;
    @NotNull
    private String targetType;
//    @NotNull
//    private String content;
}
