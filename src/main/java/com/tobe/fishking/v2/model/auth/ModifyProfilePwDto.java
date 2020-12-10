package com.tobe.fishking.v2.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ModifyProfilePwDto {
    private String currentPw;
    private String newPw;
}
