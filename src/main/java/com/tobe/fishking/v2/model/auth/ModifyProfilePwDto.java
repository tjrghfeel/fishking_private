package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyProfilePwDto {
    @Pattern(regexp = Constants.PW, message = "비밀번호는 영문, 숫자, 특수문자($@$!%*#?&)를 하나 이상 포함하여 8자 이상이어야 합니다.")
    @NotNull(message = "비밀번호가 비었습니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Size(max = 100, message = "비밀번호는 100자를 넘을 수 없습니다.")
    private String currentPw;
    @Pattern(regexp = Constants.PW, message = "비밀번호는 영문, 숫자, 특수문자($@$!%*#?&)를 하나 이상 포함하여 8자 이상이어야 합니다.")
    @NotNull(message = "비밀번호가 비었습니다")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Size(max = 100, message = "비밀번호는 100자를 넘을 수 없습니다.")
    private String newPw;
}
