package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.CouponType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "Coupon")
@Table(name = "coupon") //coupon
public class Coupon extends BaseTime {
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', coupon

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO //mssql
    public Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'쿠폰유형', 'USER', DBO, 'TABLE', coupon, 'COLUMN', coupon_type
    @Column(columnDefinition = "int   comment '할인쿠폰유형 - 정액/정률'  ")  //정률없음
    @Enumerated(EnumType.ORDINAL) //ORDINAL -> int로 할당 STRING -> 문자열로 할당
    private CouponType couponType;

    // EXEC sp_addextendedproperty 'MS_Description', N'쿠폰코드', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  couponCode

    //생성규칙  VIP/General(V/G)  + A(정액, R:정률) +  '00'  +  발행일자(6)  + 일련번호 (7)\
    //ex) GA00201231000000
    @Column(columnDefinition = "varchar(20)  comment '쿠폰코드'  ")
    public String couponCreateCode;

    // EXEC sp_addextendedproperty 'MS_Description', N'쿠폰명', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  couponName
    @Column(columnDefinition = "varchar(50)  comment '쿠폰명'  ")
    public String couponName;

    // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-시작', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  exposure_start_date
    @Column(columnDefinition = "datetime   comment '노출기간-시작 - 발행일자'  ")
    private LocalDate exposureStartDate;

    // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-종료', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  exposure_end_date
    //만료일자 23:59까지 발행 가능
    @Column(columnDefinition = "datetime  comment '노출기간-종료 -만료일자'  ")
    private LocalDate exposureEndDate;


    // EXEC sp_addextendedproperty 'MS_Description', N'할인금액(율)', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  saleValues
    @Column(columnDefinition = "int  comment '할인금액(율)'  ")
    private Integer saleValues;

    // EXEC sp_addextendedproperty 'MS_Description', N'최대발행갯수', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  maxIssue
    //발행갯수가 최대발행갯수를 넘으면 발행중지
    @Column(columnDefinition = "int  comment '최대발행갯수'  ")
    private Integer maxIssueCount;

    // EXEC sp_addextendedproperty 'MS_Description', N'유효일수', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  effective_days
    @Column(columnDefinition = "datetime comment '유효기간 시작'  ")
    private LocalDate effectiveStartDate;
    @Column(columnDefinition = "datetime  comment '유효기간 종료'  ")
    private LocalDate effectiveEndDate;

    // EXEC sp_addextendedproperty 'MS_Description', N'발행수량', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  issue_qty
    //발행수량은  최대발행수량은 넘지 않게
    @Column(columnDefinition = "int comment '발행수량'")
    private Integer issueQty;

    // EXEC sp_addextendedproperty 'MS_Description', N'사용여부(중지)', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  use_qty
    @Column(columnDefinition = "int  comment '사용수량'")
    private Integer useQty;

    // EXEC sp_addextendedproperty 'MS_Description', N'발행여부', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  is_issue
    @Column(columnDefinition = "bit default 1  comment '발행/발행중지'")
    private Boolean isIssue;

    // EXEC sp_addextendedproperty 'MS_Description', N'사용여부(중지)', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  is_use
    @Column(columnDefinition = "bit default 1  comment '사용/사용중지'")
    private Boolean isUse;

    //--condition (ex: 100000 구매이상) 0원은 모두 쿠폰 배포

    // EXEC sp_addextendedproperty 'MS_Description', N'구매금액 0이면 구매금액 상관없음', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  from_purchase_amount
    @Column(columnDefinition = "int  comment '구매금액 0이면 구매금액 상관없음' ")
    private Integer fromPurchaseAmount;

    // EXEC sp_addextendedproperty 'MS_Description', N'구매금액 0이면 구매금액 상관없음', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  to_purchase_amount
    @Column(columnDefinition = "int  comment '구매금액 0이면 구매금액 상관없음' ")
    private Integer toPurchaseAmount;

    // EXEC sp_addextendedproperty 'MS_Description', N'설명', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  introduce
    @Column(columnDefinition = "varchar(200)   comment '간략소개'  ")
    private String brfIntroduction;

    // EXEC sp_addextendedproperty 'MS_Description', N'설명', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  description
    @Column(columnDefinition = "varchar(4000)   comment '설명-유의/재한사항'  ")
    private String couponDescription;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', coupon, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = " bigint not null comment '수정자' ")
    private Member modifiedBy;

}
