package com.tobe.fishking.v2.model.board;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPageDto {
    private String title;
    private Integer commentCount;
    private List<CommentDtoForPage> commentList;
}
