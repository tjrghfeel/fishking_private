package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*Post 수정요청시 프론트로부터 받아올 DTO. */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdatePostDTO {
    //not null필드.
    private Long postId;
    private Long boardId;
    private Integer channelType;
    private String title;
    private String contents;
    //private Long authorId;//modifiedBy, createdBy필드를 여기서 id로 가져와 사용할것.
    //    private String authorName;
    private Integer returnType;
    private String returnNoAddress;
    private String createdAt;
    private Integer questionType;

    //nullable 필드.
//    private List<String> tagsName = new ArrayList<>();
    private Boolean isSecret;
    //private Long parentId;

}
