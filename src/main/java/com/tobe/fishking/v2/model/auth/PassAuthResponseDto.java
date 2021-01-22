package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassAuthResponseDto {
    private String memberName;
    private String areaCode;
    private String localNumber;
    private String passLoginId;
}
