package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppleLoginPayLoadDto {
    private String iss;
    private String aud;
    private Long exp;
    private Long iat;
    private String sub;
    private String nonce;
    private String c_hash;
    private String at_hash;
    private String email;
    private String email_verified;
    private String is_private_email;
    private Long auth_time;
    private boolean nonce_supported;
}
