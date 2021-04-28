package com.tobe.fishking.v2.model.admin.member;

import com.tobe.fishking.v2.enums.Constants;
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
    @Pattern(regexp = Constants.EMAIL, message = "이메일 형식이 잘못되었습니다")
    private String uid;

    @NotNull(message = "닉네임이 비었습니다")
    @Size(min=1, message = "닉네임이 비었습니다")
    @Size(max = 10, message = "닉네임은 100자 이하이어야합니다")
    private String nickName;

    @NotNull(message = "비밀번호가 비었습니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야합니다")
    @Size(max=20, message = "비밀번호는 20자 이하이어야합니다")
    @Pattern(regexp = Constants.PW,
            message = "비밀번호는 특수문자,숫자,영어 포함 8자 이상 14자 이하이어야 합니다. ")
    private String pw;

    @NotNull(message = "회원 유형이 비었습니다")
    private String role;
}
