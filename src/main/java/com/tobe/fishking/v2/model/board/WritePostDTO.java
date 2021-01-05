package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.board.Tag;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private String channelType;//
    private String title;//
    private String contents;//
//    private String authorName;
    private String returnType;
    private String returnNoAddress;
    private String createdAt;
    private String questionType;

    //nullable 필드.
//    private List<String> tagsName = new ArrayList<>();
    private Boolean isSecret;
    private Long parentId;

    private Long[] files;//

    public WritePostDTO(Post post) {
        boardId = post.getBoard().getId();
        channelType = post.getChannelType().getValue();
        title = post.getTitle();
        contents = post.getContents();
//        authorName = post.getAuthorName();
        returnType = post.getReturnType().getValue();
        returnNoAddress = post.getReturnNoAddress();
        createdAt = post.getCreatedAt();
        questionType = post.getQuestionType().getValue();

        //nullable 필드.
        isSecret = post.getIsSecret();
        parentId = post.getParent_id();
        List<Tag> tagList = post.getTags();
        /*for(int i=0; i<tagList.size(); i++){
            tagsName.add(tagList.get(i).getTagName());
        }*/
    }


}

