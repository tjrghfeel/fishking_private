package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto {
    private Long memberId;

//    @Email(message = "이메일 형식이 잘못되었습니다") no validator 오류떠서 아래 정규식으로 검증하는걸로 변경.
    @Pattern(regexp = Constants.EMAIL, message = "이메일 형식이 잘못되었습니다")
    @Size(max=50, message = "이메일은 50자를 넘을 수 없습니다.")
    @NotNull(message = "이메일이 비었습니다")
    private String email;

    @NotNull(message = "비밀번호가 비었습니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Size(max = 100, message = "비밀번호는 100자를 넘을 수 없습니다.")
    @Pattern(regexp = Constants.PW, message = "비밀번호는 영문, 숫자, 특수문자($@$!%*#?&)를 각각 하나 이상 포함하여 8자 이상이어야 합니다.")
    private String pw;

    @NotNull(message = "닉네임이 비었습니다")
    @Pattern(regexp = Constants.STRING, message = "닉네임은 한글, 영어 또는 숫자로 구성되어야 합니다.")
    @Size(min = 2, max = 10, message = "닉네임은 4~10자이어야 합니다.")
    private String nickName;

    private String registrationToken;

    /*@NotNull(message = "휴대폰 인증 id가 없습니다")
    private Long phoneAuthId;*/

    /*sns를 통하여 회원가입하는 경우 추가되는 필드. */
    /*private String snsId;
    private String snsType;

    @NotNull
    private String memberName;
    @NotNull
    private String areaCode;
    @NotNull
    private String localNumber;
    @NotNull
    private String passLoginId;*/
}
