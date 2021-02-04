package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.common.ShareStatus;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "event") //event

public class Event extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', event, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'이벤트명', 'USER', DBO, 'TABLE', event, 'COLUMN',  title
    @Column(columnDefinition = "varchar(50)  comment '이벤트명'  ")
    private String title;

    // EXEC sp_addextendedproperty 'MS_Description', N'이벤트내용', 'USER', DBO, 'TABLE', event, 'COLUMN',  contents
    @Column(columnDefinition = "varchar(200)  comment '이벤트내용'  ")
    private String contents;


    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', event, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(columnDefinition = "int NOT NULL comment '선상'  ")
    private Ship ship;

    @Column(columnDefinition = "varchar(20) not null comment '이벤트 시작일'")
    private String startDay;
    @Column(columnDefinition = "varchar(20) not null comment '이벤트 종료일'")
    private String endDay;

    @Embedded
    private ShareStatus status;

    @Column(columnDefinition = "bit not null default 0 comment '삭제여부'")
    private Boolean isDeleted;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', event, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', event, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;

}
