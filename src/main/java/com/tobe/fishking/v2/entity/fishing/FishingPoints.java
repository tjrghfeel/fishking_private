package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "fishing_point") //갯바위 포인트
public class FishingPoints {

    @Id
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  id
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(updatable=false,nullable=false , columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'제목', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  title
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(50)   comment '제목'  ")
    private String title;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  address
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(200)   comment '주소'  ")
    private String address;

    // EXEC sp_addextendedproperty 'MS_Description', N'위도', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  latitude
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '위도'  ")
    private Long latitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'경도', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  longitude
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '경도'  ")
    private Long longitude;


    // EXEC sp_addextendedproperty 'MS_Description', N'수심', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  depth_of_water
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '수심'  ")
    private Long depthOfWater;



    // EXEC sp_addextendedproperty 'MS_Description', N'저질', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  quality_of_land
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(100)   comment '저질'  ")
    private String qualityOfLand;


    // EXEC sp_addextendedproperty 'MS_Description', N'적정물때', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  prop_water_time
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(100)   comment '적정물때'  ")
    private String propWaterTime;

    // EXEC sp_addextendedproperty 'MS_Description', N'기준위치와의거리', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  distance_from_base_point
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(100)   comment '기준위치와의거리'  ")
    private String distanceFromBasePoint;


    // EXEC sp_addextendedproperty 'MS_Description', N'대표어종과평균수온', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  speciesAndTemperature
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(4000)   comment '대표어종과평균수온'  ")
    private String speciesAndTemperature;


    // EXEC sp_addextendedproperty 'MS_Description', N'갯바위-포인트', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  goods
    @ManyToOne
    @JoinColumn(columnDefinition = "int NOT NULL comment '갯바위-포인트'  ")
    private Goods goods;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', fishing_point, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null comment '수정자' ")
    private Member modifiedBy;


}
