package com.tobe.fishking.v2.entity.board;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.board.BoardType;
import com.tobe.fishking.v2.enums.board.FilePublish;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Board")
@Table(name = "board")
public class Board extends BaseTime implements Serializable {

    /* board type
     *   기본 : 제목, 작성자, 비밀번호, 내용, 첨부 1
     *   갤러리 : 이미지 첨부만 가능, 첨부를 thumbnail로 처리
     *   자료실 : 첨부 5개까지 가능
     *   Q&A : 비밀글 표시 및 비밀번호 검사 후 내용 표시
     *   공지사항 : 글쓰기 버튼 비활성화, 관리자만 쓰기 가능
     *   방명록 : 쓰기, 삭제, 목록보기 기능
     * url    : 링크를 누르면 이동할 페이지의 주소
     * secret : 비밀글 표시 사용 여부
     * read_allow  : 읽기 권한 (all, login, customer, business, admin)
     * write_allow : 쓰기 권한 (all, login, customer, business, admin)
     * reply  : 댓글 권한 (all, login, customer, business, admin)
     * modify : 수정 권한 (writer, admin)
     * remove : 삭제 권한 (writer, admin)
     * download : 첨부물 다운로드 권한 (all, login, customer, business, admin)
     * upload : 첨부물 업로드 권한 (all, login, customer, business, admin)
     * nAttach : 첨부 파일 수
     * aSize : 첨부파일 하나의 크기 제약
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', board, 'COLUMN',  id
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    //@Column(columnDefinition = " varchar(255) null comment '게시판 유형'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'게시판 유형', 'USER', DBO, 'TABLE', board, 'COLUMN',  board_type
    @Column(columnDefinition = " varchar(100) null comment '게시판 유형' ")
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    //@Column(length = 100, nullable = false)
    // @Column(columnDefinition = " varchar(40) not null comment '게시판명'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'게시판명', 'USER', DBO, 'TABLE', board, 'COLUMN',  board_name
    @Column(columnDefinition = " varchar(40) not null  comment '게시판명' ")
    private String name;

    // @Column(columnDefinition = " varchar(100) not null comment 'URL'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'URL', 'USER', DBO, 'TABLE', board, 'COLUMN',  url
    @Column(columnDefinition = " varchar(100) not null  comment 'URL' ")
    private String url;


    //@Column(columnDefinition = " char(1)  default 'F' not null comment '비밀여부'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'비밀여부', 'USER', DBO, 'TABLE', board, 'COLUMN',  secret
    @Column(columnDefinition = " char(1)  default 'F' not null   comment '비밀여부' ")
    private String secret;

    // @Column(columnDefinition = " varchar(10)  comment '읽기권한'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'읽기권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  read_allow
    @Column(columnDefinition = " varchar(10)   comment '읽기권한' ")
    private String readAllow;

    //@Column(columnDefinition = " varchar(10)  comment '쓰기권한'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'쓰기권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  write_allow
    @Column(columnDefinition = " varchar(10)  comment '쓰기권한' ")
    private String writeAllow;

    //@Column(columnDefinition = " varchar(10)  comment '댓글권한'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'댓글권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  reply_allow
    @Column(columnDefinition = " varchar(10)  comment '댓글권한' ")
    private String replyAllow;

    //@Column(columnDefinition = " varchar(10)  comment '수정권한'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'수정권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  modify_allow
    @Column(columnDefinition = " varchar(10) comment '수정권한' ")
    private String modifyAllow;

 //   @Column(columnDefinition = " varchar(10)  comment '삭제권한'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'삭제권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  remove
    @Column(columnDefinition = " varchar(10) comment '삭제권한' ")
    private String remove;


    // @Column(columnDefinition = " varchar(100) not null comment 'URL'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'업로드경로', 'USER', DBO, 'TABLE', board, 'COLUMN',  upload_path
    @Column(columnDefinition = " varchar(100) not null ")
    private String uploadPath;

    //@Column(columnDefinition = " varchar(10)  comment '첨부물 다운로드권한 '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'첨부파일 다운로드권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  download
    @Column(columnDefinition = " varchar(10) comment '첨부파일 다운로드권한 ' ")
    private String download;


    // @Column(columnDefinition = " varchar(100) not null comment 'URL'  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'다운로드경로', 'USER', DBO, 'TABLE', board, 'COLUMN',  download_path
    @Column(columnDefinition = " varchar(100) not null ")
    private String downloadPath;

    //@Column(columnDefinition = " varchar(10)  comment '첨부물 업로드권한 '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'첨부파일 업로드권한', 'USER', DBO, 'TABLE', board, 'COLUMN',  upload
    @Column(columnDefinition = " varchar(10)   null  comment '첨부파일 업로드권한' ")
    private String upload;

    // mysql > @Column(columnDefinition = " int(2) default 0 not null   comment '첨부파일수 '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'첨부파일수', 'USER', DBO, 'TABLE', board, 'COLUMN',  n_attach
    @Column(columnDefinition = "int  null comment '첨부파일수' ")
    private int nAttach;

   // @Column(columnDefinition = " varchar(5)  comment '첨부파일 하나의 크기 제약 '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'첨부파일 하나의 크기 제약', 'USER', DBO, 'TABLE', board, 'COLUMN',  a_size
    @Column(columnDefinition = "varchar(5) null comment ' 첨부파일 하나의 크기 제약' ")
    private String aSize;

    // @Column(columnDefinition = " int(1) default 1 not null comment '포맷 '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'포맷', 'USER', DBO, 'TABLE', board, 'COLUMN',  display_format
    @Column(columnDefinition = "int  default 1 not null comment '포맷' ")
    private  int displayFrmat;

    //myssql @Column(columnDefinition = " varchar(100)  comment '게시판 description '  ")
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'게시판 description', 'USER', DBO, 'TABLE', board, 'COLUMN',  board_desc
    @Column(columnDefinition = "varchar(100) null comment '게시판 description' ")
    private String boardDesc;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', board, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', board, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


    @Enumerated(EnumType.ORDINAL)
    @Column(unique = true, name = "file_publish")
    private FilePublish filePublish;

    FilePublish getFilePublish() {
        return filePublish;
    }

    public String getPublish() {
        return filePublish.toString();
    }

}
