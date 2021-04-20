package com.tobe.fishking.v2.model.admin.member;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeTempMemberDto {
    @NotNull(message = "uid가 비었습니다")
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 잘못되었습니다")
    private String uid;
    @Size(min = 4, max = 10)
    private String nickName;
    @Size(min = 8, max = 16, message = "비밀번호는 8~14자")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$",
            message = "비밀번호는 특수문자,숫자,영어 포함 8자 이상 14자 이하이어야 합니다. ")
    private String pw;
    @NotNull(message = "회원 유형이 비었습니다")
    private String role;
}
