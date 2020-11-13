package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/*Post 수정요청시 프론트로부터 받아올 DTO. */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdatePostDTO {
    private Long postId;
    private String contents;
    private String createdAt;
    private boolean secret;
    private Long parentId;
    private String title;

}
