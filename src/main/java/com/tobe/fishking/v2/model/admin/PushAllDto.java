package com.tobe.fishking.v2.model.admin;

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
public class PushAllDto {
    @NotNull(message = "제목이 비었습니다")
    @Size(max=50, message = "제목은 50자 이내이어야 합니다")
    @Pattern(regexp = Constants.STRING, message = "제목은 특수문자를 포함하실 수 없습니다.")
    private String title;
    @NotNull(message = "내용이 비었습니다")
    @Size(max=255, message = "내용은 255자 이내이어야 합니다")
    @Pattern(regexp = Constants.STRING, message = "내용에는 특수문자를 포함하실 수 없습니다.")
    private String content;
}
