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
public class ModifyingStatusMessageDto {
    @Pattern(regexp = Constants.STRING, message = "상태메시지는 한글, 영어 또는 숫자로 구성되어야 합니다.")
    @Size(max=60, message = "상태메시지는 60자를 넘을 수 없습니다.")
    private String statusMessage;
}
