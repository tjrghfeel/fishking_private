package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.SearchPublish;

import javax.persistence.*;

public class Popular extends BaseTime {


    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', review, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    // @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;


    // EXEC sp_addextendedproperty 'MS_Description', N'업로드(어선,게시판등)구분', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_publish
    @Column(nullable = false , columnDefinition = "int   comment '구분 : 전체검색, 업체, 조행기, 조황일지, 어복TV '  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private SearchPublish searchPublish;


    // EXEC sp_addextendedproperty 'MS_Description', N'업로드(어선,게시판등)구분', 'USER', DBO, 'TABLE', files, 'COLUMN',  file_publish
    @Column(nullable = false , columnDefinition = "varchar(100)   comment '검색어'  ")
    private String  searchKeyWord;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', review, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null  comment '생성자'")
    private Member createdBy;

    public Popular(SearchPublish searchPublish, String searchKeyWord, Member createdBy) {
        this.searchPublish = searchPublish;
        this.searchKeyWord = searchKeyWord;
        this.createdBy = createdBy;
    }
}
