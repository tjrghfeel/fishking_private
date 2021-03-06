package com.tobe.fishking.v2.entity;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "files")
public class FileEntity extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'업로드(어선,게시판등)구분', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_publish
    @Column(nullable = false , columnDefinition = "int   comment '업로드(어선,게시판,Comment등)구분'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FilePublish filePublish;

    // EXEC sp_addextendedproperty 'MS_Description', N'상위번호', 'USER', DBO, 'TABLE', files, 'COLUMN',  bid
    @Column(columnDefinition = "int   comment '상위번호'  ")
    private int parent_id;

    // EXEC sp_addextendedproperty 'MS_Description', N'상위번호', 'USER', DBO, 'TABLE', files, 'COLUMN',  bid
    @Column(columnDefinition = "int   comment '번호 - board'  ")
    private Long pid;


    // EXEC sp_addextendedproperty 'MS_Description', N'순번', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_no
    @Column(nullable = false , columnDefinition = "int default 0  comment  '순번'  ")
    private int fileNo;

    // @Column(columnDefinition = "int comment '파일형태'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'파일형태', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_type
    @Column(nullable = false , columnDefinition = "int   comment '파일형태'  ")
    @Enumerated(EnumType.ORDINAL)
    private FileType fileType;



    //_socer => soccer.jpg
    // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_name
    @Column(columnDefinition = "varchar(100) comment '파일명'  ")
    private String fileName;

    //_org
    // EXEC sp_addextendedproperty 'MS_Description', N'원본파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  original_file
    @Column(columnDefinition = "varchar(100) comment '원본파일명'  ")
    private String originalFile;

    //_adfdfdfdsfdsfdsfd.jpg

   // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  stored_file
   @Column(columnDefinition = "varchar(100) comment '저장파일명'  ")
    private String  storedFile;

   //_thumb.jpg
    // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  thumname_file
    @Column(columnDefinition = "varchar(100) comment '썸네일'  ")
    private String  thumbnailFile;

    @Column(columnDefinition = "varchar(150) comment '섬네일 다운url'  ")
    private String  downloadThumbnailUrl;

//    @Column(columnDefinition = "varchar(100) comment '업로드URL'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'업로드URL', 'USER', DBO, 'TABLE', files, 'COLUMN',  url
    @Column(columnDefinition = "varchar(100) comment '업로드URL' ")
    private String fileUrl;

    //    @Column(columnDefinition = "varchar(100) comment '업로드URL'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'업로드URL', 'USER', DBO, 'TABLE', files, 'COLUMN',  url
    @Column(columnDefinition = "varchar(100) comment '다운로드URL' ")
    private String downloadUrl;


    // @Column(columnDefinition = "integer default 0 not null comment '파일크기'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'파일크기', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_size
    @Column(columnDefinition = "integer default 0 not null comment '파일크기' ")
    private long size;

    //@Column(columnDefinition = "boolean default false not null comment '삭제여부'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'삭제여부', 'USER', DBO, 'TABLE', files, 'COLUMN',  is_delete
    @Column(columnDefinition = "bit default 0 not null comment '삭제여부' ") //mssql 은 false 안됨 , 0만 된다.
    private boolean isDelete;

    // EXEC sp_addextendedproperty 'MS_Description', N'설명', 'USER', DBO, 'TABLE', files, 'COLUMN',  caption
    @Column(columnDefinition = "varchar(500) comment '설명' ")
    private String caption;

    // EXEC sp_addextendedproperty 'MS_Description', N'촬영위치', 'USER', DBO, 'TABLE', files, 'COLUMN',  created_date
    // @Column(columnDefinition = "bit default false not null ")
    @Column(columnDefinition = "varchar(200) default 0 not null comment '촬영위치'") //mssql 은 false 안됨 , 0만 된다.
    private String  locations;

    // EXEC sp_addextendedproperty 'MS_Description', N'caption위치 - default 아래', 'USER', DBO, 'TABLE', files, 'COLUMN',  created_date
    @Column(columnDefinition = "bit default 0 not null comment 'Caption위치 - default 아래' ") //mssql 은 false 안됨 , 0만 된다.
    private boolean isTop;

    @Column(columnDefinition = "bit default 0 not null comment '대표여부' ") //mssql 은 false 안됨 , 0만 된다.
    private boolean isRepresent;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', files, 'COLUMN',  created_by
    @ManyToOne/*(cascade = CascadeType.ALL)*/
    @JoinColumn(name="created_by" , updatable= false , columnDefinition = "bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', files, 'COLUMN',  modified_by
    @ManyToOne/*(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE})*/
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint not null comment '수정자'")
    private Member modifiedBy;

    /*미리보기용으로 임시저장(isDelete필드가 true인상태)된 파일을 일반저장(isDelete필드 false)으로 변경*/
    public void saveTemporaryFile(Long pid){
        this.pid = pid;
        this.isDelete=false;
    }

    /*isRepresent필드 변경*/
    public void setRepresent(Boolean value){this.isRepresent =value;}
    /*삭제처리*/
    public void setIsDelete(boolean value){this.isDelete=value;}



}
