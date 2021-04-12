package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutDto {
    private String registrationToken;
}
