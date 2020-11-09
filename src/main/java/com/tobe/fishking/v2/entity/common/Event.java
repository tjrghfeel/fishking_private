package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Ship;
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


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', event, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', event, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;

}
