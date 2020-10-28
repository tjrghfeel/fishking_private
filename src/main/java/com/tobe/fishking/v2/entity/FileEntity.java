package com.tobe.fishking.v2.entity;


import com.tobe.fishking.v2.entity.auth.Member;
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
    private int pid;

    // EXEC sp_addextendedproperty 'MS_Description', N'번호', 'USER', DBO, 'TABLE', files, 'COLUMN',  bid
    @Column(nullable = false , columnDefinition = "int  comment '번호'  ")
    private int bid;

    // EXEC sp_addextendedproperty 'MS_Description', N'순번', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_no
    @Column(nullable = false , columnDefinition = "int default 0  comment  '순번'  ")
    private int fileNo;

    // @Column(columnDefinition = "int comment '파일형태'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'파일형태', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_type
    @Column(nullable = false , columnDefinition = "int   comment '파일형태'  ")
    @Enumerated(EnumType.STRING)
    private FileType fileType;



    // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_name
    @Column(columnDefinition = "varchar(100) comment '파일명'  ")
    private String fileName;



    // EXEC sp_addextendedproperty 'MS_Description', N'원본파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  original_file
    @Column(columnDefinition = "varchar(100) comment '원본파일명'  ")
    private String originalFile;


   // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  stored_file
   @Column(columnDefinition = "varchar(50) comment '저장파일명'  ")
    private String  storedFile;


    // EXEC sp_addextendedproperty 'MS_Description', N'파일명', 'USER', DBO, 'TABLE', files, 'COLUMN',  thumname_file
    @Column(columnDefinition = "varchar(50) comment '썸네일'  ")
    private String  thumbnailFile;

//    @Column(columnDefinition = "varchar(100) comment '업로드URL'  ")
    // EXEC sp_addextendedproperty 'MS_Description', N'업로드URL', 'USER', DBO, 'TABLE', files, 'COLUMN',  url
    @Column(columnDefinition = "varchar(100) comment '업로드URL' ")
    private String fileUrl;



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


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', files, 'COLUMN',  created_by
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = "bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', files, 'COLUMN',  modified_by
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint not null comment '수정자'")
    private Member modifiedBy;

}
