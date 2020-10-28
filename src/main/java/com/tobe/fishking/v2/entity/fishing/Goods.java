package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "goods") //상품정보
public class Goods extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', goods, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    // @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'상품명', 'USER', DBO, 'TABLE', goods, 'COLUMN',  name
    @Column(columnDefinition = "varchar(100)   comment '상품명'  ")
    private String name;

    // EXEC sp_addextendedproperty 'MS_Description', N'구분 : 선상/갯바위', 'USER', DBO, 'TABLE', goods, 'COLUMN',  when
    @Column(columnDefinition = "int   comment '구분:선상/갯바위'  ")
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private FishingType fishingType;

    // EXEC sp_addextendedproperty 'MS_Description', N'일자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_date
    @Column(columnDefinition = "varchar(8) comment  '일자'  ")
    private String fishingDate;

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
    private double totalAmount;


    // EXEC sp_addextendedproperty 'MS_Description', N'정원(min)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  max_personnel
    @Column(columnDefinition = "int comment '정원(min)'  ")
    private int minPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'정원(max)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  max_personnel
    @Column(columnDefinition = "int  comment  '정원(max)'  ")
    private int maxPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'마감여부', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_close
    @Column(nullable = false, columnDefinition = "bit default 0 comment '마감여부'  ")
    private boolean isClose;

    // EXEC sp_addextendedproperty 'MS_Description', N'예약인수', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column( columnDefinition = "float comment '예약인수'  ")
    private double orderPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'대기인', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column( columnDefinition = "float comment '대기인수'  ")
    private double watingPersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'초보가능여부', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_close
    @Column(nullable = false, columnDefinition = "bit default 1 comment  '초보가능여부'") //comment '초보가능여부'  ")
    private boolean begginerPossible;

    // EXEC sp_addextendedproperty 'MS_Description', N'상태(노출여부)', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(nullable = false,  columnDefinition = "bit default 0  comment  '상태(노출여부)'  ")
    private boolean isVisible;

    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "goods_fish_species", columnDefinition = " comment  '상품 어종'  ")
    private final List<CommonCode> fishSpecies = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'선상-상품목록', 'USER', DBO, 'TABLE', goods, 'COLUMN',  fishing_goods
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "goods_ship_id", columnDefinition = "bigint   not null comment '선상-상품목록'  ")
    private Ship  ship ;

    // EXEC sp_addextendedproperty 'MS_Description', N'상품정보-미끼목록', 'USER', DBO, 'TABLE', goods, 'COLUMN',  lure
    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "goods_fish_lure ", columnDefinition = " comment  '루어낚시'  ")
    private final List<CommonCode> lure = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'전체평균평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  total_average
    @Column(columnDefinition = "float  default 0.0 comment  '전체평균평점'  ")
    private double totalAvgByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'손맛평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '손맛평점'  ")
    private double tasteByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'서비스평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '서비스평점'  ")
    private double serviceByReview;
    // EXEC sp_addextendedproperty 'MS_Description', N'청결도평점', 'USER', DBO, 'TABLE', goods, 'COLUMN',  is_visible
    @Column(columnDefinition = "float  default 0.0  comment  '청결도평점'  ")
    private double cleanByReview;

    // EXEC sp_addextendedproperty 'MS_Description', N'적립포인트', 'USER', DBO, 'TABLE', goods, 'COLUMN',  accumulate_point
    @Column(columnDefinition = "float  default 0.0  comment  '적립포인트'  ")
    private double accumulatePoint;

    // EXEC sp_addextendedproperty 'MS_Description', N'공지사항', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "varchar(200) comment '공지사항'  ")
    private String notice;


    // EXEC sp_addextendedproperty 'MS_Description', N'구매', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column(columnDefinition = "varchar(200) comment '구매'  ")
    private String onSitePurchase;

    // EXEC sp_addextendedproperty 'MS_Description', N'기타', 'USER', DBO, 'TABLE', goods, 'COLUMN',  notice
    @Column( columnDefinition = "varchar(1000) comment '기타'  ")
    private String etc;

    // EXEC sp_addextendedproperty 'MS_Description', N'장소 소개', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  orders_id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="goods_place_id" , columnDefinition = "bigint comment  '장소 소개'")
    private Places places;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null comment '생성자'")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;



}
