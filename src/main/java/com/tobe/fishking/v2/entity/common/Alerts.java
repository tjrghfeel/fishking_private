package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.AlertType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "alerts")
//EXEC sp_addextendedproperty 'MS_Description', N'알림', 'USER', DBO, 'TABLE', alerts,
public class Alerts  {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    /*공통코드는   CodeGroup :  AlertSet / AlertUnSet    */
    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "alert_sets", columnDefinition = " comment  '알람셋'  ")
    @Builder.Default
    private List<CommonCode> alert_sets = new ArrayList<>();

    //파일은  FileEntity에 저장을 하고, FilePublish는 Alert

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', alerts, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;

}
