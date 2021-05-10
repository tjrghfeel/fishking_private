package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsLoginResponseDto {
    private String snsType;
    private String resultType;
    private Long memberId;
    private String sessionToken;
    private Boolean isError;
    private String errorCode;
    private String errorMessage;
}
