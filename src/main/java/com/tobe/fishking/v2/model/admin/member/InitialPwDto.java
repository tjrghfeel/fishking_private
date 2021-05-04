package com.tobe.fishking.v2.model.admin.member;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InitialPwDto {
    @NotNull(message = "회원id가 비었습니다")
    private Long memberId;

    @NotNull(message = "비밀번호가 비었습니다.")
    @Pattern(regexp = Constants.PW, message = "비밀번호는 특수문자,숫자,영어 포함 8자 이상 14자 이하이어야 합니다. ")
    private String pw;
//    @NotNull(message = "비밀번호확인이 비었습니다.")
//    @Pattern(regexp = Constants.PW, message = "비밀번호는 특수문자,숫자,영어 포함 8자 이상 14자 이하이어야 합니다. ")
//    private String pwConfirm;
}
