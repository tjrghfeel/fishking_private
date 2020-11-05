package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
//1:1문의 글쓰기할때 프론트로부터 받아오는 dto.
public class WritePostDTO {
    //not null필드.
    private Long boardId;
    private String channelType;//원래는 enum타입이지만 view단에서 데이터 넘겨주기에는 string이 편할것같아 String으로 함.
    private String title;
    private String contents;
    private Long authorId;
    private String returnType;//channelType과 마찬가지.
    private String returnNoAddress;
    private String createdAt;

    //nullable 필드.
    private List<String> tagsName = new ArrayList<>();
    private boolean isSecret;
    private Long parentId;

    public WritePostDTO(){}

    public WritePostDTO(Post post) {
        boardId = post.getBoard().getId();
        channelType = post.getChannelType().getValue();
        title = post.getTitle();
        contents = post.getContents();
        authorId = post.getAuthor().getId();
        returnType = post.getReturnType().getValue();
        returnNoAddress = post.getReturnNoAddress();
        createdAt = post.getCreatedAt();

        //nullable 필드.
        isSecret = post.isSecret();
        parentId = post.getParent_id();
        List<Tag> tagList = post.getTags();
        for(int i=0; i<tagList.size(); i++){
            tagsName.add(tagList.get(i).getTagName());
        }
    }


}

