package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDTO {
    //not null필드.
    private Long id;
    private Long boardId;
    private int channelType;
    private String title;
    private String contents;
    private Long authorId;
    private String authorName;
    private int returnType;
    private String returnNoAddress;
    private String createdAt;
    private Long createdById;
    private Long modifiedById;
    private int questionType;

    //nullable 필드.
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<String> tagsName = new ArrayList<>();
    private boolean isSecret;
    private Long parentId;
    private int likeCount;

    public PostDTO(){}

    public PostDTO(Post post) {
        id = post.getId();
        boardId = post.getBoard().getId();
        channelType = post.getChannelType().ordinal();
        title = post.getTitle();
        contents = post.getContents();
        authorId = post.getAuthor().getId();
        authorName = post.getAuthorName();
        returnType = post.getReturnType().ordinal();
        returnNoAddress = post.getReturnNoAddress();
        createdAt = post.getCreatedAt();
        createdById = post.getCreatedBy().getId();
        modifiedById = post.getModifiedBy().getId();
        questionType = post.getQuestionType().ordinal();

        //nullable 필드.
        likeCount = post.getLikeCount();
        isSecret = post.isSecret();
        parentId = post.getParent_id();
        List<Tag> tagList = post.getTags();
        for(int i=0; i<tagList.size(); i++){
            tagsName.add(tagList.get(i).getTagName());
        }
    }


}
