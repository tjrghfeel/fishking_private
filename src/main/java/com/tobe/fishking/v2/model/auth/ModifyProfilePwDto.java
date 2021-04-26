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
public class ModifyProfilePwDto {
    @Pattern(regexp = Constants.PW)
    @Size(max=150)
    private String currentPw;
    @Pattern(regexp = Constants.PW)
    @Size(max=150)
    private String newPw;
}
