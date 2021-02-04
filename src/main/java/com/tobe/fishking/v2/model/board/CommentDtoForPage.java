package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDtoForPage {
    private Long authorId;
    private Long commentId;
    private String profileImage;
    private String nickName;
    private LocalDateTime writeTime;
    private String content;
    private String fileUrl;
    private Integer likeCount;
    private Boolean isLikeTo;
    private Boolean isChildComment;
    private Long parentId;
    private List<CommentDtoForPage> childList;
    private Boolean isMine;

    public CommentDtoForPage(
            Long authorId, Long commentId, String profileImage, String nickName, LocalDateTime writeTime,
            String content, String fileUrl, Integer likeCount, Boolean isLikeTo, Boolean isChildComment, Long parentId,
            Boolean isMine
    ){
        this.authorId=authorId; this.commentId=commentId; this.profileImage= profileImage; this.nickName=nickName;
        this.writeTime=writeTime; this.content=content; this.fileUrl=fileUrl; this.likeCount=likeCount;
        this.isLikeTo=isLikeTo; this.isChildComment=isChildComment; this.parentId=parentId; this.isMine=isMine;
    }
}
