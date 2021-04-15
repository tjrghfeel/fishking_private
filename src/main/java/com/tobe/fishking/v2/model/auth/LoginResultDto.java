package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResultDto {
    private Boolean auth;
    private String token;
    private Long memberId;
}
