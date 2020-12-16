package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyProfilePwDto {
    private String currentPw;
    private String newPw;
}
