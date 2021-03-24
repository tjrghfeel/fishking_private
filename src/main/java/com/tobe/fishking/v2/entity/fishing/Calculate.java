package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calculate extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO //mssql
    @Column(updatable=false, nullable=false, columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders", columnDefinition = "bigint comment '예약' ")
    private Orders orders;

    @Column(columnDefinition = "varchar(4) comment '년도' ")
    private String year;

    @Column(columnDefinition = "varchar(2) comment '월' ")
    private String month;

    @Column(columnDefinition = "bigint comment '가격'")
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "ship", columnDefinition = "bigint comment '선박' ")
    private Ship ship;

    @Column(columnDefinition = "bit comment '취소여부'")
    private Boolean isCancel;

    @Column(columnDefinition = "bit default 0 comment '정산여부'")
    private Boolean isCalculate = false;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint comment '수정자'  ")
    private Member modifiedBy;

    @Builder
    public Calculate(Orders orders, String year, String month, Ship ship, Boolean isCancel) {
        this.orders = orders;
        this.year = year;
        this.month = month;
        this.amount = Long.valueOf(orders.getTotalAmount());
        this.ship = ship;
        this.isCancel = isCancel;
    }

    public void setCalculate(Member member) {
        this.isCalculate = true;
        this.modifiedBy = member;
    }

    public void setUnCalculate(Member member) {
        this.isCalculate = false;
        this.modifiedBy = member;
    }

}
