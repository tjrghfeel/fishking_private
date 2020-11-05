package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.board.BoardType;
import com.tobe.fishking.v2.enums.board.FilePublish;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private BoardType boardType;
    private String name;
    private String url;
    private String secret;
    private String readAllow;
    private String writeAllow;
    private String replyAllow;
    private String modifyAllow;
    private String remove;
    private String uploadPath;
    private String download;
    private String downloadPath;
    private String upload;
    private int nAttach;
    private String aSize;
    private  int displayFrmat;
    private String boardDesc;
    private Member createdBy;
    private Member modifiedBy;
    private FilePublish filePublish;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    FilePublish getFilePublish() {
        return filePublish;
    }

    public String getPublish() {
        return filePublish.toString();
    }

}
