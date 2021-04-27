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
public class ModifyingNickNameDto {
    @NotNull(message = "닉네임이 비었습니다")
    @Size(min = 4, max = 10, message = "닉네임은 4~10자이어야 합니다.")
    @Pattern(regexp = Constants.STRING, message = "닉네임은 한글, 영어 또는 숫자로 구성되어야 합니다.")
    private String nickName;
}
