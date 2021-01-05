package com.tobe.fishking.v2.model.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/*Post 수정요청시 프론트로부터 받아올 DTO. */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostDTO {
    //not null필드.
    private Long postId;
    private Long boardId;
    private String channelType;
    private String title;
    private String contents;
    //private Long authorId;//modifiedBy, createdBy필드를 여기서 id로 가져와 사용할것.
    //    private String authorName;
    private String returnType;
    private String returnNoAddress;
    private String createdAt;
    private String questionType;

    //nullable 필드.
//    private List<String> tagsName = new ArrayList<>();
    private Boolean isSecret;
    //private Long parentId;

    private Long[] files;
}
