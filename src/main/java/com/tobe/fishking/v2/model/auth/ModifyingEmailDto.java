package com.tobe.fishking.v2.model.auth;

import com.tobe.fishking.v2.enums.Constants;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyingEmailDto {
    @Pattern(regexp = Constants.EMAIL, message = "이메일 형식이 잘못되었습니다")
    @Size(max=50, message = "이메일은 50자가 넘을 수 없습니다.")
    private String email;
}
