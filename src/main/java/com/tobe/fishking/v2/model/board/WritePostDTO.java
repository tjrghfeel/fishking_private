package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//1:1문의 글쓰기할때 프론트로부터 받아오는 dto.
public class WritePostDTO {
    //not null필드.
    private Long boardId;
    private int channelType;
    private String title;
    private String contents;
    private Long authorId;//modifiedBy, createdBy필드를 여기서 id로 가져와 사용할것.
//    private String authorName;
    private int returnType;
    private String returnNoAddress;
    private String createdAt;
    private int questionType;

    //nullable 필드.
    private List<String> tagsName = new ArrayList<>();
    private boolean isSecret;
    private Long parentId;

    public WritePostDTO(Post post) {
        boardId = post.getBoard().getId();
        channelType = post.getChannelType().ordinal();
        title = post.getTitle();
        contents = post.getContents();
        authorId = post.getAuthor().getId();
//        authorName = post.getAuthorName();
        returnType = post.getReturnType().ordinal();
        returnNoAddress = post.getReturnNoAddress();
        createdAt = post.getCreatedAt();
        questionType = post.getQuestionType().ordinal();

        //nullable 필드.
        isSecret = post.isSecret();
        parentId = post.getParent_id();
        List<Tag> tagList = post.getTags();
        for(int i=0; i<tagList.size(); i++){
            tagsName.add(tagList.get(i).getTagName());
        }
    }


}

