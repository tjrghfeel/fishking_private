package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "zip_code")
public class ZipCode extends BaseTime implements Serializable {
    
    
    // 우편번호|우편일련번호|시도|시도영문|시군구|시군구영문|읍면|읍면영문|
    // EXEC sp_addextendedproperty 'MS_Description', N'code', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  id
    @Id
    private String code;
    @Id
    // EXEC sp_addextendedproperty 'MS_Description', N'우편번호', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  zipcode
    @Column(columnDefinition = "varchar(6)   comment '우편번호'  ")
    private String zipcode;

    // EXEC sp_addextendedproperty 'MS_Description', N'우편일련번호', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  zipseq
    @Column(columnDefinition = "varchar(10)   comment '우편일련번호'  ")
    private String zipseq;

    // EXEC sp_addextendedproperty 'MS_Description', N'시도', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  sido
    @Column(columnDefinition = "varchar(30)   comment '시도'  ")
    private String sido;


    // EXEC sp_addextendedproperty 'MS_Description', N'시도영문', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  sido_eng
    @Column(columnDefinition = "varchar(30)   comment '시도영문'  ")
    private String sido_eng;

    // EXEC sp_addextendedproperty 'MS_Description', N'시군구', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  sigungu
    @Column(columnDefinition = "varchar(30)   comment '시군구'  ")
    private String sigungu;


    // EXEC sp_addextendedproperty 'MS_Description', N'시군구영문', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  sigungu_eng
    @Column(columnDefinition = "varchar(30)   comment '시군구영문'  ")
    private String sigungu_eng;


    // EXEC sp_addextendedproperty 'MS_Description', N'읍면', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  eupmyun
    @Column(columnDefinition = "varchar(30)   comment '읍면'  ")
    private String eupmyun;

    // EXEC sp_addextendedproperty 'MS_Description', N'읍면영문', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  eupmyun_eng
    @Column(columnDefinition = "varchar(30)   comment '읍면영문'  ")
    private String eupmyun_eng;

    //|도로명코드|도로명|도로명영문|지하여부|건물번호본번|건물번호부번|건물관리번호|다량배달처명|시군구용건물명|법정동코드|법정동명|


    // EXEC sp_addextendedproperty 'MS_Description', N'도로명코드', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  streetcode
    @Column(columnDefinition = "varchar(12)   comment '도로명코드'  ")
    private String streetcode;

    // EXEC sp_addextendedproperty 'MS_Description', N'도로명', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  street
    @Column(columnDefinition = "varchar(90)   comment '도로명'  ")
    private String street;


    // EXEC sp_addextendedproperty 'MS_Description', N'도로명영문', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  street_eng
    @Column(columnDefinition = "varchar(90)   comment '도로명영문'  ")
    private String street_eng;


    // EXEC sp_addextendedproperty 'MS_Description', N'지하여부 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  isunder
    @Column(columnDefinition = "char(1)   comment '지하여부'  ")
    private String isunder;

    // EXEC sp_addextendedproperty 'MS_Description', N'건물번호본번', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(5)   comment '건물번호본번'  ")
    private String buildingnum1;


    // EXEC sp_addextendedproperty 'MS_Description', N'건물번호부번', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(5)   comment '건물번호부번'  ")
    private String buildingnum2;


    // EXEC sp_addextendedproperty 'MS_Description', N'건물관리번호', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(25)   comment '건물관리번호'  ")
    private String buildingcode;

    // EXEC sp_addextendedproperty 'MS_Description', N'다량배달처명', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(60)   comment '다량배달처명'  ")
    private String massdestination;

    // EXEC sp_addextendedproperty 'MS_Description', N'시군구용건물명', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(50)   comment '시군구용건물명'  ")
    private String building;

    // EXEC sp_addextendedproperty 'MS_Description', N'법정동코드', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(10)   comment '법정동코드'  ")
    private String dongcode;

    // EXEC sp_addextendedproperty 'MS_Description', N'법정동명', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  address
    @Column(columnDefinition = "varchar(30)   comment '법정동명'  ")
    private String dong;

    //|리|산여부|지번본번|읍면동일련번호|지번부번
    // EXEC sp_addextendedproperty 'MS_Description', N'리', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  ri
    @Column(columnDefinition = "varchar(30)   comment '리'  ")
    private String ri;

    // EXEC sp_addextendedproperty 'MS_Description', N'산여부', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  ismountain
    @Column(columnDefinition = "char(1)   comment '산여부'  ")
    private String ismountain;


    // EXEC sp_addextendedproperty 'MS_Description', N'지번본번', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  jibun1
    @Column(columnDefinition = "int   comment '지번본번'  ")
    private int jibun1;

    // EXEC sp_addextendedproperty 'MS_Description', N'읍면동일련번호', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  dongseq
    @Column(columnDefinition = "varchar(2)   comment '읍면동일련번호'  ")
    private String dongseq;

    // EXEC sp_addextendedproperty 'MS_Description', N'지번부번', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  jibun2
    @Column(columnDefinition = "int   comment '지번부번'  ")
    private int jibun2;



    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by", updatable = false, columnDefinition = " bigint NOT NULL   comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', zip_code, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint  NOT NULL   comment '수정자' ")
    private Member modifiedBy;


}
