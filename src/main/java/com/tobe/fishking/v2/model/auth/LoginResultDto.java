package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResultDto {
    private Boolean isCertified;
    private String sessionToken;
    private Long memberId;
}
