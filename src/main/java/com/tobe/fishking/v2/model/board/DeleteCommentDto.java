package com.tobe.fishking.v2.model.board;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteCommentDto {
    @NotNull
    private Long commentId;
}
