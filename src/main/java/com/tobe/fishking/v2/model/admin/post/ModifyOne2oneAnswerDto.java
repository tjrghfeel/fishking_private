package com.tobe.fishking.v2.model.admin.post;

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
public class ModifyOne2oneAnswerDto {
    @NotNull
    private Long answerPostId;
    @NotNull(message = "내용을 입력하세요")
    @Size(min=1, message = "내용을 입력하세요")
    @Size(max=2000, message = "내용은 2000자 이하이어야합니다")
    @Pattern(regexp = Constants.STRING)
    private String content;
    @Size(min=0, max=20, message = "파일 개수는 20개 이하이어야합니다")
    private Long[] fileList;
}
