package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "ship")
public class Ship extends BaseTime {  //선상
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'선박명', 'USER', DBO, 'TABLE', ship, 'COLUMN',  name
    @Column(columnDefinition = "varchar(50) comment '선박명'  ")
    private String shipName;
/*
    @OneToMany(targetEntity=Goods.class, mappedBy="ship")
    @OrderBy("name ASC")
    private Set<Goods> goods = new HashSet<Goods>();

    public Set<Goods> getGoods() {
        return goods;
    }

    public void setGoods(Set<Goods> goods) {
        this.goods = goods;
    }
*/
    @Column(columnDefinition = "int   comment '구분:선상/갯바위'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FishingType fishingType;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', ship, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '주소'  ")
    private String address;


    // EXEC sp_addextendedproperty 'MS_Description', N'시도', 'USER', DBO, 'TABLE', ship, 'COLUMN',  sido
    @Column(columnDefinition = "varchar(30)   comment '시도'  ")
    private String sido;

    // EXEC sp_addextendedproperty 'MS_Description', N'시군구', 'USER', DBO, 'TABLE', ship, 'COLUMN',  sigungu
    @Column(columnDefinition = "varchar(30)   comment '시군구'  ")
    private String sigungu;

    // EXEC sp_addextendedproperty 'MS_Description', N'지역별', 'USER', DBO, 'TABLE', ship, 'COLUMN',  by_region
    @Column(columnDefinition = "int comment '지역별' ", nullable = false)
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private SeaDirection seaDirection;

    // EXEC sp_addextendedproperty 'MS_Description', N'중량', 'USER', DBO, 'TABLE', ship, 'COLUMN',  weight
    @Column(columnDefinition = "decimal(5,2)  comment  '거리'  ")
    private Double distance;

    // EXEC sp_addextendedproperty 'MS_Description', N'연락처', 'USER', DBO, 'TABLE', ship, 'COLUMN',  tel
    @Column(columnDefinition = "varchar(30)   comment '연락처'  ")
    private String tel;

// EXEC sp_addextendedproperty 'MS_Description', N'중량', 'USER', DBO, 'TABLE', ship, 'COLUMN',  weight
    @Column(columnDefinition = "decimal(5,2)  comment  '중량'  ")
    private Double weight;


    // EXEC sp_addextendedproperty 'MS_Description', N'탑승인원', 'USER', DBO, 'TABLE', ship, 'COLUMN',  boarding_person
    @Column(columnDefinition = "int  comment '탑승인원'  ")
    private Integer boardingPerson;


    // EXEC sp_addextendedproperty 'MS_Description', N'추천업체', 'USER', DBO, 'TABLE', ship, 'COLUMN',  is_recommend
    @Column(nullable = false, columnDefinition = "int default 0  comment '추천업체'  ")
    private Boolean isRecommend;

    // EXEC sp_addextendedproperty 'MS_Description', N'사장님한마디', 'USER', DBO, 'TABLE', ship, 'COLUMN',  owner_wording
    @Column(columnDefinition = "varchar(500) comment '사장님한마디'  ")
    private String ownerWording;

    //녹화영상 FileEntity

    // EXEC sp_addextendedproperty 'MS_Description', N'어종', 'USER', DBO, 'TABLE', fishing_ship, 'COLUMN',  fishing_rtvideos
    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "ships_fish_species", columnDefinition = "comment '어종'  ")
    private final List<CommonCode> fishSpecies = new ArrayList<>();


    // EXEC sp_addextendedproperty 'MS_Description', N'실시간영상', 'USER', DBO, 'TABLE', fishing_ship, 'COLUMN',  fishing_rtvideos
    @OneToMany
    @JoinColumn(name = "ship_rtvideos_id" , columnDefinition = "bigint  comment '실시간영상'  ")
    //  @Builder.Default
    private final List<RealTimeVideo> shiipRealTimeVideos = new ArrayList<>();


    // EXEC sp_addextendedproperty 'MS_Description', N'업체', 'USER', DBO, 'TABLE', ship, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(columnDefinition = "bigint  NOT NULL   comment '업체'")
    private Company company;

    // EXEC sp_addextendedproperty 'MS_Description', N'전체평균평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  total_average
    @Column(columnDefinition = "float  default 0.0 comment  '전체평균평점'  ")
    private Double totalAvgByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'손맛평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '손맛평점'  ")
    private Double tasteByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'서비스평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '서비스평점'  ")
    private Double serviceByReview;
    // EXEC sp_addextendedproperty 'MS_Description', N'청결도평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '청결도평점'  ")
    private Double cleanByReview;
    @Column(columnDefinition = "int default 0 comment '리뷰 수'")
    private int reviewCount;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ship, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ship, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    public Ship(String shipName, Company company, Member member, SeaDirection seaDirection ) {
        this.shipName = shipName;
        this.company = company;
        this.createdBy = member;
        this.modifiedBy = member;
        this.seaDirection = seaDirection;


    }

    /*리뷰 평점 적용*/
    public void applyReviewGrade(double taste, double clean, double service){
        this.tasteByReview = (reviewCount*this.tasteByReview + taste)/(reviewCount+1);
        this.cleanByReview = (reviewCount*this.cleanByReview + clean)/(reviewCount+1);
        this.serviceByReview = (reviewCount*this.serviceByReview + service)/(reviewCount+1);
        this.totalAvgByReview = (this.tasteByReview+this.cleanByReview+this.serviceByReview)/3;
        this.reviewCount++;
    }
}
