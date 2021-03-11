package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.fishing.FishSpecies;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
//import com.tobe.fishking.v2.model.fishing.ParamsGoods;
import com.tobe.fishking.v2.enums.fishing.ReserveType;
import com.tobe.fishking.v2.model.fishing.AddGoods;
import com.tobe.fishking.v2.model.fishing.ParamsGoods;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
@Table(name = "goods") //상품정보
public class Goods extends BaseTime {


    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', goods, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY //mssql
    // @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable = false, nullable = false, columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'상품명', 'USER', DBO, 'TABLE', goods, 'COLUMN',  name
    @Column(columnDefinition = "varchar(100)   comment '상품명'  ")
    private String name;

    // EXEC sp_addextendedproperty 'MS_Description', N'구분 : 선상/갯바위', 'USER', DBO, 'TABLE', goods, 'COLUMN',  when
    /*@Column(columnDefinition = "int   comment '구분:선상/갯바위'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FishingType fishingType;*/

    // EXEC sp_addextendedproperty 'MS_Description', N'일자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_date
    @Column(columnDefinition = "varchar(8) comment  '일자-출항일'  ")
    private String fishingDate;

    @OneToMany(mappedBy = "goods")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GoodsFishingDate> fishingDates;

    // EXEC sp_addextendedproperty 'MS_Description', N'배출발시간', 'USER', DBO, 'TABLE', goods, 'COLUMN',  ship_start_time
    @Column(columnDefinition = "varchar(4) comment  '배출발시간'  ")
    private String shipStartTime;

    // EXEC sp_addextendedproperty 'MS_Description', N'구분 : 오전-오후', 'USER', DBO, 'TABLE', goods, 'COLUMN',  when
    @Column(columnDefinition = "int   comment '구분:오전-오후'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private Meridiem meridiem;

    // EXEC sp_addextendedproperty 'MS_Description', N'낚시시작시간', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_start_time
    @Column(columnDefinition = "varchar(4) comment  '낚시시작시간'  ")
    private String fishingStartTime;

    // EXEC sp_addextendedproperty 'MS_Description', N'낚시종료시간', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_end_time
    @Column(columnDefinition = "varchar(4) comment  '낚시종료시간'  ")
    private String fishingEndTime;

    // EXEC sp_addextendedproperty 'MS_Description', N'총가격', 'USER', DBO, 'TABLE', goods, 'COLUMN',  total_amount
    @Column(columnDefinition = "float comment '총가격'  ")
    private Integer totalAmount;


    // EXEC sp_addextendedproperty 'MS_Description', N'정원(min)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  max_personnel
    @Column(columnDefinition = "int comment '정원(min)'  ")
    private Integer minPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'정원(max)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  max_personnel
    @Column(columnDefinition = "int  comment  '정원(max)'  ")
    private Integer maxPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'마감여부', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_close
    @Column(nullable = false, columnDefinition = "bit default 0 comment '마감여부'  ")
    private Boolean isClose;

    // EXEC sp_addextendedproperty 'MS_Description', N'예약인수', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "float comment '예약인수'  ")
    private Double reservationPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'대기인', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "float comment '대기인수'  ")
    private Double waitingPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'초보가능여부', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_close
    @Column(nullable = false, columnDefinition = "bit default 1 comment  '초보가능여부'") //comment '초보가능여부'  ")
    private Boolean isBeginnerPossible;

    // EXEC sp_addextendedproperty 'MS_Description', N'상태(노출여부)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(nullable = false, columnDefinition = "bit default 0  comment  '상태(노출여부)'  ")
    private Boolean isVisible;

    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "goods_fish_species", columnDefinition = " comment  '상품 어종'  ")
    @LazyCollection(LazyCollectionOption.FALSE)
    @Builder.Default
    private List<CommonCode> fishSpecies = new ArrayList<>();


    // EXEC sp_addextendedproperty 'MS_Description', N'선상-상품목록', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_goods
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_ship_id", columnDefinition = "bigint   not null comment '선상-상품목록'  ")
    private Ship ship;

    // EXEC sp_addextendedproperty 'MS_Description', N'상품정보-미끼목록', 'USER', DBO, 'TABLE', goods, 'COLUMN',  lure
    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "goods_fish_lure ", columnDefinition = " comment  '루어낚시'  ")
    @Builder.Default
    private List<CommonCode> fishingLures = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'적립포인트', 'USER', DBO, 'TABLE', goods, 'COLUMN',  accumulate_point
    @Column(columnDefinition = "float  default 0.0  comment  '적립포인트'  ")
    private Double accumulatePoint;

    // EXEC sp_addextendedproperty 'MS_Description', N'공지사항', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "varchar(200) comment '공지사항'  ")
    private String notice;


    // EXEC sp_addextendedproperty 'MS_Description', N'구매', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "varchar(200) comment '구매'  ")
    private String onSitePurchase;

    // EXEC sp_addextendedproperty 'MS_Description', N'기타', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "varchar(1000) comment '기타'  ")
    private String etc;

    // EXEC sp_addextendedproperty 'MS_Description', N'장소 소개', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  orders_id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_place_id", columnDefinition = "bigint comment  '장소 소개'")
    private Places places;

    // EXEC sp_addextendedproperty 'MS_Description', N'상태(노출여부)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(nullable = false, columnDefinition = "bit default 1  comment  '사용여부'  ")
    private Boolean isUse;

    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "goods_facility", columnDefinition = " comment  '편의시설'  ")
    @Builder.Default
    private List<CommonCode> facilities = new ArrayList<>();


    @ManyToMany(targetEntity = CommonCode.class)
    @JoinColumn(name = "goods_nearby_facility", columnDefinition = " comment  '주변시설'  ")
    @Builder.Default
    private List<CommonCode> nearbyFacilities = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'추천업체', 'USER', DBO, 'TABLE', ship, 'COLUMN',  is_recommend
    @Column(nullable = false, columnDefinition = "int default 0  comment '추천업체'  ")
    private Boolean isRecommend;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name = "created_by", insertable = false, updatable = false, columnDefinition = " bigint not null comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name = "modified_by", insertable = false, updatable = false, columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Column(columnDefinition = " varchar(100)  comment '물때'  ")
    private String fishingTideTime;

    @ManyToMany(targetEntity = CommonCode.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "goods_genres", columnDefinition = " comment  '장르'  ")
    @Builder.Default
    private List<CommonCode> genres = new ArrayList<>();

    @Column(columnDefinition = "int comment '예약타입' ")
    @Enumerated(EnumType.ORDINAL)
    private ReserveType reserveType;

    @Column(columnDefinition = "bit default 1 comment '예약자 위치선정' ")
    private Boolean positionSelect;

    @Column(columnDefinition = "bit default 1 comment '추가운행여부' ")
    private Boolean extraRun;

    @Column(columnDefinition = "int comment '추가 운항 최소 인원 수 ' ")
    private Integer extraPersonnel;

    @Column(columnDefinition = "int comment '최대 선박 수 ' ")
    private Integer extraShipNumber;


    @Builder
    public Goods(Ship ship, Member member, AddGoods addGoods, List<CommonCode> fishSpecies) {
        this.ship = ship;
        this.name = addGoods.getName();
        this.fishingStartTime = addGoods.getFishingStartTime();
        this.fishingEndTime = addGoods.getFishingEndTime();
        this.totalAmount = addGoods.getAmount();
        this.minPersonnel = addGoods.getMinPersonnel();
        this.maxPersonnel = addGoods.getMaxPersonnel();
        this.isUse = addGoods.getIsUse();
        this.fishSpecies = fishSpecies;
        this.reserveType = addGoods.getReserveType().equals("auto") ? ReserveType.auto : ReserveType.approval;
        this.positionSelect = addGoods.getPositionSelect();
        this.extraRun = addGoods.getExtraRun();
        this.extraPersonnel = addGoods.getExtraPersonnel();
        this.extraShipNumber = addGoods.getExtraShipNumber();
        this.createdBy = member;
        this.modifiedBy = member;
    }

    // 생성자
    public Goods(Member member, Ship ship, ParamsGoods paramsGoods) {
        this.createdBy = member;
        this.modifiedBy = member;
        this.name = paramsGoods.getGoodsName();
//        this.fishingType = paramsGoods.getFishingType();
        this.fishingDate = paramsGoods.getFishingDate();
        this.shipStartTime = paramsGoods.getShipStartTime();
        this.meridiem = paramsGoods.getMeridiem();
        this.fishingStartTime = paramsGoods.getFishingStartTime();
        this.fishingEndTime = paramsGoods.getFishingEndTime();
        this.totalAmount = paramsGoods.getTotalAmount();
        this.minPersonnel = paramsGoods.getMinPersonnel();
        this.maxPersonnel = paramsGoods.getMaxPersonnel();
        this.isClose = paramsGoods.isClose();
        this.reservationPersonnel = paramsGoods.getReservationPersonnel();
        this.waitingPersonnel = paramsGoods.getWaitingPersonnel();
        this.isBeginnerPossible = paramsGoods.isBeginnerPossible();
        this.isVisible = paramsGoods.isVisible();
        this.ship = ship;

        //this.fishSpecies = paramsGoods.getFishSpecies();
        //this.fishingLures = paramsGoods.getFishingLures();

//        this.totalAvgByReview = paramsGoods.getTotalAvgByReview();
//        this.tasteByReview = paramsGoods.getTasteByReview();
//        this.serviceByReview = paramsGoods.getServiceByReview();
//        this.cleanByReview = paramsGoods.getCleanByReview();
        this.accumulatePoint = paramsGoods.getAccumulatePoint();
        this.notice = paramsGoods.getNotice();
        this.onSitePurchase = paramsGoods.getOnSitePurchase();
        this.etc = paramsGoods.getEtc();
        this.places = places;
    }

    public void setFishSpecies(List<CommonCode> fishSpecies) {
        this.fishSpecies = fishSpecies;
    }

    public void setFishingLures(List<CommonCode> fishingLures) {
        this.fishingLures = fishingLures;
    }

    public Goods() {

    }

    public Goods(Member member,  Ship ship,  String name, FishingType fishingType, List<CommonCode> arrFishSpecies ) {
        this.modifiedBy = member;
        this.name = name;
//        this.fishingType = fishingType;
        this.ship = ship;

    }





    // 수정시 데이터 처리
    public Goods setUpdate(Member member, Ship ship, Places places, ParamsGoods paramsGoods) {

        this.modifiedBy = member;

        this.name = paramsGoods.getGoodsName();
//        this.fishingType = paramsGoods.getFishingType();
        this.fishingDate = paramsGoods.getFishingDate();
        this.shipStartTime = paramsGoods.getShipStartTime();
        this.meridiem = paramsGoods.getMeridiem();
        this.fishingStartTime = paramsGoods.getFishingStartTime();
        this.fishingEndTime = paramsGoods.getFishingEndTime();
        this.totalAmount = paramsGoods.getTotalAmount();
        this.minPersonnel = paramsGoods.getMinPersonnel();
        this.maxPersonnel = paramsGoods.getMaxPersonnel();
        this.isClose = paramsGoods.isClose();
        this.reservationPersonnel = paramsGoods.getReservationPersonnel();
        this.waitingPersonnel = paramsGoods.getWaitingPersonnel();
        this.isBeginnerPossible = paramsGoods.isBeginnerPossible();
        this.isVisible = paramsGoods.isVisible();
        this.ship = ship;

        //this.fishSpecies = paramsGoods.getFishSpecies();
        //this.fishingLures = paramsGoods.getFishingLures();

//        this.totalAvgByReview = paramsGoods.getTotalAvgByReview();
//        this.tasteByReview = paramsGoods.getTasteByReview();
//        this.serviceByReview = paramsGoods.getServiceByReview();
//        this.cleanByReview = paramsGoods.getCleanByReview();
        this.accumulatePoint = paramsGoods.getAccumulatePoint();
        this.notice = paramsGoods.getNotice();
        this.onSitePurchase = paramsGoods.getOnSitePurchase();
        this.etc = paramsGoods.getEtc();
        this.places = places;
        return this;
    }



}
