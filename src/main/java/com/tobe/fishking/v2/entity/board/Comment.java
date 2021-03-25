package com.tobe.fishking.v2.entity.board;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    @Enumerated(EnumType.ORDINAL)
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

    @Column(columnDefinition = "int comment '좋아요수' ")
    private Integer likeCount;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = "bit not null default 0 comment '삭제여부'")
    private Boolean isDeleted;

    @Column(columnDefinition = "bit default 1 comment '숨김처리 여부'")
    private Boolean isActive;

    /*@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("post_id")
    private Post post;*/

    public void plusLikeCount(){this.likeCount++;}
    public void subLikeCount(){this.likeCount--;}
    public void delete(){this.isDeleted = true;}

    public void modify(String content){
        this.contents = content;
    }
    public void setActive(Boolean active){this.isActive = active;}
}
