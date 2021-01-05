package com.tobe.fishking.v2.model.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAlertDto {
    private Long memberId;
    private String alertType;
    private String entityType;
    private Long pid;
    private Long createdBy;
    private String content;
}
