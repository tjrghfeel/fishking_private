package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResultDtoForSmartFishing {
    private Long memberId;
    private Boolean auth;
    private Boolean isRegistered;
    private String token;

}
