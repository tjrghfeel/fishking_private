package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.common.Location;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor

@Table(name = "places")   //갯바위
public class Places extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', place, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    //프사 사진은  FileEntity
    // EXEC sp_addextendedproperty 'MS_Description', N'상품명', 'USER', DBO, 'TABLE', place, 'COLUMN',  place_name
    @Column(columnDefinition = "varchar(100)   comment '장소명'  ")
    private String palceName;

    // EXEC sp_addextendedproperty 'MS_Description', N'시도', 'USER', DBO, 'TABLE', place, 'COLUMN',  sido
    @Column(columnDefinition = "varchar(30)   comment '시도'  ")
    private String sido;

    // EXEC sp_addextendedproperty 'MS_Description', N'시군구', 'USER', DBO, 'TABLE', place, 'COLUMN',  sigungu
    @Column(columnDefinition = "varchar(30)   comment '시군구'  ")
    private String sigungu;

    // EXEC sp_addextendedproperty 'MS_Description', N'시군구', 'USER', DBO, 'TABLE', place, 'COLUMN',  sigungu
    @Column(columnDefinition = "varchar(200)   comment '주소'  ")
    private String address;


    // EXEC sp_addextendedproperty 'MS_Description', N'시군구', 'USER', DBO, 'TABLE', place, 'COLUMN',  sigungu
    @Column(columnDefinition = "varchar(30)   comment '위치'  ")
    private Location location;


    
    
    // EXEC sp_addextendedproperty 'MS_Description', N'기본정보', 'USER', DBO, 'TABLE', place, 'COLUMN',  base_info
    @Column(columnDefinition = "varchar(1000) comment  '기본정보'  ")
    private String baseInfo;

    // EXEC sp_addextendedproperty 'MS_Description', N'대상어종', 'USER', DBO, 'TABLE', place, 'COLUMN',  fish_spices_info
    @Column(columnDefinition = "varchar(1000) comment  '대상어종'  ")
    private String fishSpicesInfo;

    // EXEC sp_addextendedproperty 'MS_Description', N'지역소개', 'USER', DBO, 'TABLE', place, 'COLUMN',  region_introduction
    @Column(columnDefinition = "varchar(1000) comment  '지역소개'  ")
    private String regionIntroduction;


    // EXEC sp_addextendedproperty 'MS_Description', N'주의사항', 'USER', DBO, 'TABLE', place, 'COLUMN',  precautions
    @Column(columnDefinition = "varchar(1000) comment  '주의사항'  ")
    private String precautions;


    // EXEC sp_addextendedproperty 'MS_Description', N'기상조류', 'USER', DBO, 'TABLE', place, 'COLUMN',  weather_tide
    @Column(columnDefinition = "varchar(1000) comment  '기상조류'  ")
    private String weatherTide;


    // EXEC sp_addextendedproperty 'MS_Description', N'기상조류', 'USER', DBO, 'TABLE', place, 'COLUMN',  weather_tide
    @Column(columnDefinition = "varchar(1000) comment  '계절별 정보 (대상어종, 평균수온) '  ")
    private String seasonInfo;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,   updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;



    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

}
