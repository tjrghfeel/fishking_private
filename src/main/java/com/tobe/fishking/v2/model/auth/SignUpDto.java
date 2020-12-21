package com.tobe.fishking.v2.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignUpDto {
//    @Email(message = "이메일 형식이 잘못되었습니다") no validator 오류떠서 아래 정규식으로 검증하는걸로 변경.
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 잘못되었습니다")
    @NotNull(message = "이메일이 비었습니다")
    private String email;

    @NotNull(message = "비밀번호가 비었습니다")
    @Size(min = 8, max = 16, message = "비밀번호는 8~14자")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String pw;

    @NotNull(message = "닉네임이 비었습니다")
    @Size(min = 4, max = 10)
    private String nickName;


    /*퍼블리싱 화면에는 위 세개만 있지만, 엔터티를보면 더필요할듯하다. 추후 추가. */
    @NotNull(message = "휴대폰 번호가 비었습니다. ")
    private String areaCode;
    @NotNull(message = "휴대폰 번호가 비었습니다. ")
    private String localNumber;

}
