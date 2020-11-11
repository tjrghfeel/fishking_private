package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "TideTime")
@Table(name = "tide_time")
public class TideTime extends BaseTime {

    // ALTER TABLE tide_time COMMENT = '물때';
    // ALTER TABLE tide_time MODIFY id  bigint NOT NULL AUTO_INCREMENT 'id';

    /*
     */
    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'지역', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  region
    @Column(columnDefinition = " varchar(20) not null comment '지역'  ")
    private String region;


    //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'내용', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  harbor
    @Column(columnDefinition = " varchar(1000) not null comment '항구'  ")
    private String harbor;

    // EXEC sp_addextendedproperty 'MS_Description', N'위치정보', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  location_code
    @Column(columnDefinition = "varchar(200)  comment '위치정보'  ")
    private String locationCode; //

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', tide_time, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


   //댓글은 Comment  구분장 dependentType 은 조화잉ㄹ지

}
