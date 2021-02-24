package com.tobe.fishking.v2.model.admin.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostManageDetailDto {
    private Long postId;
    private Long boardId;
    private String boardName;
    private Long parentId;
    private String channelType;
    private String questionType;
    private String title;
    private String content;
    private Long authorId;
    private String authorNickName;
    private String returnType;
    private String returnNoAddress;
    private String createdAt;
    private Boolean isSecret;
    private Long createdById;
    private String createdByNickName;
    private Long modifiedById;
    private String modifiedByNickName;
    private String targetRole;
    private Boolean isReplied;
}
