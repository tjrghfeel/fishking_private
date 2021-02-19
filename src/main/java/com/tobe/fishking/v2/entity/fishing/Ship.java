package com.tobe.fishking.v2.entity.fishing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.common.ShareStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
//@Builder
@AllArgsConstructor
@Table(name = "ship")
public class Ship extends BaseTime {  //선상
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    //@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
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
    @OneToMany(mappedBy = "ship")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Goods> goods;

    @Column(columnDefinition = "int   comment '구분:선상/갯바위'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FishingType fishingType;

    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', ship, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '선박주소'  ")
    private String address;


    // EXEC sp_addextendedproperty 'MS_Description', N'주소', 'USER', DBO, 'TABLE', ship, 'COLUMN',  address
    @Column(columnDefinition = "varchar(100)   comment '선장'  ")
    private String captain;


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
    @Column(columnDefinition = "int  comment '탑승인원-정원'  ")
    private Integer boardingPerson;


    // EXEC sp_addextendedproperty 'MS_Description', N'추천업체', 'USER', DBO, 'TABLE', ship, 'COLUMN',  is_recommend
    @Column(nullable = false, columnDefinition = "int default 0  comment '추천업체'  ")
    private Boolean isRecommend;

    @Column(columnDefinition = "varchar(500) comment '사장님한마디 제목'  ")
    private String ownerWordingTitle;

    // EXEC sp_addextendedproperty 'MS_Description', N'사장님한마디', 'USER', DBO, 'TABLE', ship, 'COLUMN',  owner_wording
    @Column(columnDefinition = "varchar(500) comment '사장님한마디'  ")
    private String ownerWording;

    @Column(columnDefinition = "varchar(500) comment '공지 제목'  ")
    private String noticeTitle;

    // EXEC sp_addextendedproperty 'MS_Description', N'사장님한마디', 'USER', DBO, 'TABLE', ship, 'COLUMN',  owner_wording
    @Column(columnDefinition = "varchar(500) comment '공지'  ")
    private String notice;


    //녹화영상 FileEntity
    // EXEC sp_addextendedproperty 'MS_Description', N'어종', 'USER', DBO, 'TABLE', fishing_ship, 'COLUMN',  fishing_rtvideos
    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "ships_fish_species", columnDefinition = "comment '어종'  ")
    @JsonBackReference
    private List<CommonCode> fishSpecies = new ArrayList<>();

    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "ships_services", columnDefinition = "comment '서비스 제공'  ")
    @JsonBackReference
    private List<CommonCode> services = new ArrayList<>();

    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "ships_facilities", columnDefinition = "comment '편의시설'  ")
    @JsonBackReference
    private List<CommonCode> facilities = new ArrayList<>();

    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "ships_devices", columnDefinition = "comment '보유장비'  ")
    @JsonBackReference
    private List<CommonCode> devices = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'실시간영상', 'USER', DBO, 'TABLE', fishing_ship, 'COLUMN',  fishing_rtvideos
    @OneToMany
    @JoinColumn(name = "ship_rtvideos_id" , columnDefinition = "bigint  comment '실시간영상'  ")
    //  @Builder.Default
    @JsonBackReference
    private final List<RealTimeVideo> shiipRealTimeVideos = new ArrayList<>();

    @Column(columnDefinition = "varchar(7) comment '관측소 번호' ")
    private String observerCode;

    @ApiModelProperty(name ="뷰-좋아요수")
    private ShareStatus status;


    @ApiModelProperty(name = "작성위치")
    private Location location;

    @ApiModelProperty(name = "실위치")
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name="latitude", column = @Column(name="liveLatitude") ),
            @AttributeOverride(name="longitude", column = @Column(name="liveLongitude") )
    } )
    private Location liveLocation;

    @Column(columnDefinition = "varchar(300) comment '선박사진 경로' ")
    private String profileImage;

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


    @Column(columnDefinition = "tinyint(1) default 1 comment '운행여부(페업)'")
    private boolean isActive;

    @Column(columnDefinition = "tinyint(1) default 0  comment '출항여부(출항중 or 정박중)'")
    private boolean departStatus;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ship, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ship, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = "varchar(100) comment '승선위치' ")
    private String positions;

//    @Builder
//    public Ship(Long id, String shipName, Company company, Member member, SeaDirection seaDirection ) {
//        this.id = id;
//        this.shipName = shipName;
//        this.company = company;
//        this.createdBy = member;
//        this.modifiedBy = member;
//        this.seaDirection = seaDirection;
//    }

    @Builder
    public Ship(String name,
                FishingType fishingType,
                String address,
                String sido,
                String sigungu,
                String tel,
                Double weight,
                Integer boardingPerson,
                String ownerWordingTitle,
                String ownerWording,
                String noticeTitle,
                String notice,
                Location location,
                String profileImage,
                Company company,
                Member createdBy,
                String code,
                String positions) {
        this.shipName = name;
        this.fishingType = fishingType;
        this.address = address;
        this.sido = sido;
        this.sigungu = sigungu;
        this.tel = tel;
        this.weight = weight;
        this.boardingPerson = boardingPerson;
        this.ownerWordingTitle = ownerWordingTitle;
        this.ownerWording = ownerWording;
        this.noticeTitle = noticeTitle;
        this.notice = notice;
        this.location = location;
        this.profileImage = profileImage;
        this.company = company;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
        this.positions = positions;
        this.isActive = true;
        this.departStatus = false;
        this.observerCode = code;
    }

    /*리뷰 평점 적용*/
    public void applyReviewGrade(double taste, double clean, double service){
        this.tasteByReview = (reviewCount*this.tasteByReview + taste)/(reviewCount+1);
        this.cleanByReview = (reviewCount*this.cleanByReview + clean)/(reviewCount+1);
        this.serviceByReview = (reviewCount*this.serviceByReview + service)/(reviewCount+1);
        this.totalAvgByReview = (this.tasteByReview+this.cleanByReview+this.serviceByReview)/3;
        this.reviewCount++;
    }

    public void setObserverCode(String code) {
        this.observerCode = code;
    }

    public void setFishSpecies(List<CommonCode> fishSpecies) {
        this.fishSpecies = fishSpecies;
    }

    public void setServices(List<CommonCode> services) {
        this.services = services;
    }

    public void setFacilities(List<CommonCode> facilities) {
        this.facilities = facilities;
    }

    public void setDevices(List<CommonCode> devices) {
        this.devices = devices;
    }

}
