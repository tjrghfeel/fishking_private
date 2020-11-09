package com.tobe.fishking.v2.entity.board;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Builder
@AllArgsConstructor

@Entity(name = "Contents")
@Table(name = "contents")
public class Contents extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', contents, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'게시판그룹', 'USER', DBO, 'TABLE', contents, 'COLUMN',  post_Id
    @ManyToOne
    @JoinColumn(columnDefinition = " bigint not null   comment '게시판'  ")
    private Post postId;

    // EXEC sp_addextendedproperty 'MS_Description', N'상위id', 'USER', DBO, 'TABLE', contents, 'COLUMN',  seq
    // @Column(columnDefinition ="INT NULL")
    @Column(nullable = false, columnDefinition ="INT  comment '순서' ")
    private Long seq;
 
    
    // EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', contents, 'COLUMN',  content
    @Column(columnDefinition = "varchar(2000) comment '내용' ", nullable = false)
    private String contents;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', contents, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', contents, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;



}
