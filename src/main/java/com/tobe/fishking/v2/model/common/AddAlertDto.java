package com.tobe.fishking.v2.model.common;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAlertDto {
    @NotNull
    private Long memberId;
    @NotNull
    private String alertType;
    private String entityType;
    private Long pid;
    private Long createdBy;
    private String content;
}
