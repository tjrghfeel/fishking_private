package com.tobe.fishking.v2.entity.fishing;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


//@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "fishing_diary_comment")
public class FishingDiaryComment extends BaseTime {

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

    @Column(columnDefinition = "bit not null default 1 comment '활성화 여부' ")
    private boolean isActive;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', comment, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fishing_diary_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @JsonProperty("fishing_diary_id")
    private FishingDiary fishingDiary;


    public FishingDiaryComment() {

    }
}
