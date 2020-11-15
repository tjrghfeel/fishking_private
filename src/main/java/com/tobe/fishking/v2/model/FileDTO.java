package com.tobe.fishking.v2.model;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FileDTO {
    //not null필드.
    private Long id;
    private int filePublish;//
    private int fileNo;
    private int fileType;//
    private boolean isDelete;
    private boolean isTop;
    private String  locations;
    private long size;
    private Long createdById;
    private Long modifiedById;

    //nullable 필드.
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String fileName;
    private Long pid;
    private String originalFile;
    private String  storedFile;
    private String  thumbnailFile;
    private String fileUrl;
    private String caption;
    private Long postId;

    public FileDTO(FileEntity fileEntity){
        id=fileEntity.getId();
        filePublish = fileEntity.getFilePublish().ordinal();
        pid = fileEntity.getPid();
        fileNo = fileEntity.getFileNo();
        fileType = fileEntity.getFileType().ordinal();
        isDelete = fileEntity.isDelete();
        isTop = fileEntity.isTop();
        locations = fileEntity.getLocations();
        createdById = fileEntity.getCreatedBy().getId();
        modifiedById = fileEntity.getModifiedBy().getId();

        fileName = fileEntity.getFileName();
        originalFile = fileEntity.getOriginalFile();
        storedFile = fileEntity.getStoredFile();
        thumbnailFile = fileEntity.getThumbnailFile();
        fileUrl = fileEntity.getFileUrl();
        size = fileEntity.getSize();
        caption = fileEntity.getCaption();





    }

}
