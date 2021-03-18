package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
//import com.tobe.fishking.v2.enums.fishing.PaymentGroup;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "orders")
public class Orders extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', orders, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO //mssql
// @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'주문일자', 'USER', DBO, 'TABLE', orders, 'COLUMN',  order_date
    @Column(columnDefinition = "varchar(10) not null   comment '주문일자'  ")
    private String orderDate;

    @Column(columnDefinition = "varchar(10) not null   comment '주문한 조업일자'  ")
    private String fishingDate;

    //(예정)승선자는  어떻게 처리?

    //최종가격  = 인원* 가격
    // EXEC sp_addextendedproperty 'MS_Description', N'최종가격', 'USER', DBO, 'TABLE', orders, 'COLUMN',  total_amount
    @Column(columnDefinition = "float comment '최종가격'  ")
    private Integer totalAmount;

    //(예정)승선자는  어떻게 처리?

    //최종가격  = 인원* 가격
    // EXEC sp_addextendedproperty 'MS_Description', N'할인금액', 'USER', DBO, 'TABLE', orders, 'COLUMN',  discount_amount
    @Column(columnDefinition = "float comment '할인금액 - 쿠폰'  ")
    private Integer discountAmount;

    // EXEC sp_addextendedproperty 'MS_Description', N'할인후결제금액', 'USER', DBO, 'TABLE', orders, 'COLUMN',  payment_amount
    @Column(columnDefinition = "float comment '할인후결제금액'  ")
    private Integer  paymentAmount;


    //결제수단 , //결제방법
 // EXEC sp_addextendedproperty 'MS_Description', N'결제수단', 'USER', DBO, 'TABLE', orders, 'COLUMN',  pay_method
    /*@Column(columnDefinition = "int comment '결제수단'  ")
    @Enumerated(EnumType.ORDINAL)
    private PaymentGroup paymentGroup;*/

    //결제여부
    // EXEC sp_addextendedproperty 'MS_Description', N'결제여부', 'USER', DBO, 'TABLE', orders, 'COLUMN',  is_pay
    @Column(columnDefinition = "bit  comment '결제여부'  ")
    private Boolean isPay;

    @Column(columnDefinition = "int  comment '결제수단'  ")
    private PayMethod payMethod;

    //OrderStatus
    // EXEC sp_addextendedproperty 'MS_Description', N'결제여부', 'USER', DBO, 'TABLE', orders, 'COLUMN',  is_pay
    @Column(columnDefinition = "int  comment '주문진행상태'  ")
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @Column(columnDefinition = "varchar(20) comment '예약번호'")
    private String ordersNum;

    @Column(columnDefinition = "varchar(30) comment '주문번호' ")
    private String orderNumber;

    @Column(columnDefinition = "varchar(30) comment '거래번호' ")
    private String tradeNumber;

    @Column(columnDefinition = "varchar(30) comment '카드사 승인번호' ")
    private String confirmNumber;

    @ManyToOne
    @JoinColumn(name = "goods", columnDefinition = "bigint comment '상품' ")
    private Goods goods;

    @Column(columnDefinition = "datetime comment '취소일시' ")
    private LocalDateTime cancelDate;

    @Column(columnDefinition = "varchar(200) comment '취소번호' ")
    private String cancelNumber;

/*

- 결제수단
 - 결제방법
 - 결제완료여부
 - 예약자
 - 상태 : 이용예정, 이용완료, 취소환불
   : 문제 임시저장? , 이용취소시 위약금 규정은
  - 예약일
  - 계약일
  - 주문일 (계약일과 동일: 임시저장 기능이 없다.)
*/
    // insertable = false, updatable = false 추가로 선언하면 MappingException은 발생하지 않는다.
    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', orders, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    // insertable = false, updatable = false 추가로 선언하면 MappingException은 발생하지 않는다.
    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', orders, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


    public void setOrderNumber(String orderNumber, Member member) {
        this.orderNumber = orderNumber;
        this.modifiedBy = member;
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void paid(Member member, String tradeNumber) {
        this.isPay = true;
        this.tradeNumber = tradeNumber;
        this.modifiedBy = member;
    }

    public void cancelled(Member member, String cancelNumber) {
        this.cancelDate = LocalDateTime.now();
        this.cancelNumber = cancelNumber;
        this.modifiedBy = member;
        this.orderStatus = OrderStatus.bookCancel;
    }

}
