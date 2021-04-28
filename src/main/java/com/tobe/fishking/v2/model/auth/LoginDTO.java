package com.tobe.fishking.v2.model.auth;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDTO {

    @NotNull(message = "id가 비었습니다")
    @Size(max = 50, message = "아이디는 50자 이하이어야합니다")
    private String memberId;
    
    @NotNull(message = "pw가 비었습니다")
    @Size(max = 100, message = "비밀번호는 100자 이하이어야합니다")
    private String password;

    private String registrationToken;
}
