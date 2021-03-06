package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryFishingLures;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "alerts")
//EXEC sp_addextendedproperty 'MS_Description', N'알림', 'USER', DBO, 'TABLE', alerts,
public class Alerts extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    /*공통코드는   CodeGroup :  AlertSet / AlertUnSet    */
    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "alert_sets", columnDefinition = " comment  '알람셋'  ")
    @Builder.Default
    private List<CommonCode> alert_sets = new ArrayList<>();

    @Column(columnDefinition = "comment null  '알람구분'  ") //분류(어떤종류의 알람인지. 새글알람인지, 쿠폰만료알람인지 등)
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private AlertType alertType;

    @Column(columnDefinition = "comment '알림과 관련있는 entity종류")
    @Enumerated(EnumType.ORDINAL)
    private EntityType entityType;

    //alerttype에 따른 해당 id
    @Column(columnDefinition = "bigint  comment '알림과 관련있는 entity의 id' ")
    private Long pid;

    @Column(columnDefinition = "varchar(30) comment '알람 데이터'")
    private String content;

    @Column(columnDefinition = "varchar(255) comment '알람 문구'")
    private String sentence;

    @Column(columnDefinition = "bit default 0 not null  comment '읽음확인' ") //0 안읽음 1:읽음
    private boolean isRead;

    @Column(columnDefinition = "bit default 0 not null comment '전송 여부'")
    private Boolean isSent;

    @LastModifiedDate  //isRead 가 업데이트되면 자동으로 저장
    @Column(columnDefinition = "datetime  comment '확인일시'  ")
    private LocalDateTime readDateTime;

    @ManyToOne
    @JoinColumn(columnDefinition = " bigint not null comment '알림받는이' ")
    public Member receiver;

    @Column(columnDefinition = "datetime comment '알림 시간'")
    private LocalDateTime alertTime;

    @Column(columnDefinition = "varchar(5) comment '알림 타입 (고객: c, 출조: f) '")
    private String type;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;

    public void sent(){this.isSent=true;}
    public void setSentence(String sentence){this.sentence = sentence;}
    public void readAlert(){this.isRead = true;}
}
