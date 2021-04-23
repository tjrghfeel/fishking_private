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
public class WriteOne2oneAnswerDto {
    @NotNull
    private Long parentId;
    @NotNull
    @Size(min=1, max=2000)
    @Pattern(regexp = Constants.STRING)
    private String content;
    @Size(min=0, max=20)
    private Long[] fileList;
}
