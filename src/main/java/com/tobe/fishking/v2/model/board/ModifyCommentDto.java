package com.tobe.fishking.v2.model.board;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyCommentDto {
    @NotNull
    private Long commentId;
    @Size(min=1,max=50,message = "내용은 1~50자이어야 합니다.")
    private String content;
    private Long fileId;
}
