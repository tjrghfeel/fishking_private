package com.tobe.fishking.v2.entity.board;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import lombok.*;

import javax.persistence.*;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "Comment")
@Table(name = "comment")
public class Comment extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', comment, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;


    // EXEC sp_addextendedproperty 'MS_Description', N'게시판유형', 'USER', DBO, 'TABLE', comment, 'COLUMN',  board_type
    @Column(columnDefinition = "int  comment '게시판종류' ")
    @Enumerated(EnumType.STRING)
    private DependentType dependentType;

    @Column(nullable = true, columnDefinition ="INT  comment 'Link ID ' ")
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', comment, 'COLUMN',  id
    // @Column(columnDefinition ="INT NOT NULL IDENTITY(1,1)")
    private Long linkId;

    // EXEC sp_addextendedproperty 'MS_Description', N'상위id', 'USER', DBO, 'TABLE', comment, 'COLUMN',  parent_id
    // @Column(columnDefinition ="INT NULL")
    @Column(nullable = true, columnDefinition ="INT  comment '상위id ' ")
    private Long parentId;

    
    // EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', comment, 'COLUMN',  content
    @Column(columnDefinition = "varchar(2000) comment '내용 ' ", nullable = false)
    private String contents;

    @Transient // DB에 영향을 미치지 않는다.
    @Column(columnDefinition = "int comment '좋아요수' ")
    private int likeCount;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;



}
