package com.tobe.fishking.v2.model.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDTO {

    private @Valid String memberId;
    private @Valid String password;
}
