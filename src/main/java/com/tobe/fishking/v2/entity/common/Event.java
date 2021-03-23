package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.model.fishing.AddEvent;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;

    @Column(columnDefinition = "int comment '이벤트 나열 순서'")
    private Integer orderLevel;

    @Column(columnDefinition = "bit comment '활성화 여부'")
    private Boolean isActive;

    @Builder
    public Event(Member member, AddEvent addEvent, Ship ship, ShareStatus status) {
        this.title = addEvent.getTitle();
        this.contents = addEvent.getTitle();
        this.ship = ship;
        this.isDeleted = false;
        this.startDay = addEvent.getStartDate();
        this.endDay = addEvent.getEndDate();
        this.status = status;
        this.createdBy = member;
        this.modifiedBy = member;
    }

    public void setDelete(Member member) {
        this.isDeleted = true;
        this.modifiedBy = member;
    }

    public void setNotDelete(Member member) {
        this.isDeleted = false;
        this.modifiedBy = member;
    }

    public Event(String title, String contents, Ship ship, String startDay, String endDay, ShareStatus status, Boolean isDeleted,
                 Member createdBy, Member modifiedBy, Boolean isActive){
        this.title = title;
        this.contents = contents;
        this.ship = ship;
        this.startDay = startDay;
        this.endDay = endDay;
        this.status = status;
        this.isDeleted =isDeleted;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.isActive = isActive;
    }

//    public void modify(
//
//    ){
//
//    }

}
