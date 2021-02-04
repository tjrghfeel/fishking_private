package com.tobe.fishking.v2.model.admin.post;

import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.common.ChannelType;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostManageDtoForPage2 {
    private Long postId;
    private Long boardId;
    private String boardName;
    private Long parentId;
    private String channelType;
    private String questionType;
    private String title;
    private String nickName;
    private Long authorId;
    private Boolean isSecret;
    private String createdDate;
    private String modifiedDate;

    public PostManageDtoForPage2(
            Post post, Board board
    ){
        this.postId = post.getId();
        this.boardId = board.getId();
        this.boardName = board.getName();
        this.parentId = post.getParent_id();
        this.channelType = post.getChannelType().getValue();
        this.questionType = post.getQuestionType().getValue();
        this.title = post.getTitle();
        this.nickName = post.getAuthor().getNickName();
        this.authorId = post.getAuthor().getId();
        this.isSecret =post.getIsSecret();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = post.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
