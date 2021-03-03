package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppleAuthCodeDto {
    private String state;
    private String code;
    private String id_token;
    private String user;
}
