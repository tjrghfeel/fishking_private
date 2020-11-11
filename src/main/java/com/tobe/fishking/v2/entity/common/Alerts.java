package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.AlertType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "Alerts")
@Table(name = "alerts")
//EXEC sp_addextendedproperty 'MS_Description', N'알림', 'USER', DBO, 'TABLE', alerts,
public class Alerts  {


    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'알림구분', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  alertType
    @Column(columnDefinition = "int  comment '알림구분-사용자새글...'  ")
    @Enumerated(EnumType.ORDINAL)
    private AlertType alertType;

    // EXEC sp_addextendedproperty 'MS_Description', N'link id', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  link_id
    @Column(columnDefinition = "int   comment 'Link Id'  ")
    private Long linkId;

   //알림설정
    // EXEC sp_addextendedproperty 'MS_Description', N'어종', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  alert_sets
    @Column(name = "alert_set_type", columnDefinition = "varchar(10)   comment '알림설정'  ")
    private String  alertSets ;

    //공통코드로 셋팅
    //EXEC sp_addextendedproperty 'MS_Description', N'알림음', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  url_sounds

    @Column(columnDefinition = "varchar(200)  comment '알림음url'  ")
    private String urlSounds;

    //알림해제
    // EXEC sp_addextendedproperty 'MS_Description', N'알림해제', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  alert_clears
    @Column(name = "alert_clears", columnDefinition = "char(1)   comment '알림해제'  ")
    private String  alertClears ;


    //생성만 있다.삭제시에는 바로 Row 삭제
    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;



}
