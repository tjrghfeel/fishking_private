package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.BannerType;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "banner") //banner
// EXEC sp_addextendedproperty 'MS_Description', N'배너', 'USER', DBO, 'TABLE', banner;
public class Banner extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', banner, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'배너명', 'USER', DBO, 'TABLE', banner, 'COLUMN',  title
    @Column(columnDefinition = "varchar(50)  comment '배너명'  ")
    private String title;

    // EXEC sp_addextendedproperty 'MS_Description', N'배너타입', 'USER', DBO, 'TABLE', banner, 'COLUMN',  bannerType
    @Column(columnDefinition = "varchar(50)  comment '배너타압명'  ")
    @Enumerated(EnumType.ORDINAL)
    private BannerType bannerType;


    // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-시작', 'USER', DBO, 'TABLE', banner, 'COLUMN',  exposure_Start_Dt
    @Column(columnDefinition = "varchar(8)   comment '노출기간-시작'  ")
    private String exposureStartDt;


    // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-시작', 'USER', DBO, 'TABLE', banner, 'COLUMN',  exposure_End_Dt
    @Column(columnDefinition = "varchar(8)   comment '노출기간-종료'  ")
    private String exposureEndDt;


    // EXEC sp_addextendedproperty 'MS_Description', N'이미지경로', 'USER', DBO, 'TABLE', banner, 'COLUMN',  imagePath
    @Column(columnDefinition = "varchar(500)   comment '이미지경로'  ")
    private String imagePath;

    // EXEC sp_addextendedproperty 'MS_Description', N'LinkURL', 'USER', DBO, 'TABLE', banner, 'COLUMN',  linkURL
    @Column(columnDefinition = "varchar(500)   comment 'LinkURL'  ")
    private String linkURL;

    // EXEC sp_addextendedproperty 'MS_Description', N'설명', 'USER', DBO, 'TABLE', banner, 'COLUMN',  description
    @Column(columnDefinition = "varchar(500)   comment '설명'  ")
    private String description;

    @Column(columnDefinition = "int comment '순서' ")
    private Integer orders;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', banner, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', banner, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint comment '수정자'")
    private Member modifiedBy;

}
