package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "orders_details")
public class OrderDetails extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // AUTO //mssql
// @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'조황정보', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "order_detail_goods_id", columnDefinition = "bigint  not null   comment '상품-조황정보'  ")
    private Goods goods;

    // EXEC sp_addextendedproperty 'MS_Description', N'조황정보', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "order_detail_orders_id", columnDefinition = "bigint  not null   comment '조황정보'  ")
    private Orders orders;

    // EXEC sp_addextendedproperty 'MS_Description', N'인원', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  personnel
    @Column(columnDefinition = "float  comment  '인원'  ")
    private Integer personnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'인원', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  personnel
    @Column(columnDefinition = "float  comment  '승선인원'  ")
    private Integer ridePersonnel;

    // EXEC sp_addextendedproperty 'MS_Description', N'가격', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  price
    @Column(columnDefinition = "float comment '가격'  ")
    private Integer price;

    //최종가격  = 인원* 가격
    // EXEC sp_addextendedproperty 'MS_Description', N'최종가격', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  total_amount
    @Column(columnDefinition = "float comment '총가격'  ")
    private Integer totalAmount;

    @Column(columnDefinition = "varchar(100) comment '승선위치' ")
    private String positions;

    @Column(columnDefinition = "bit default 0 comment '추가운행' ")
    private Boolean isExtraRun = false;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', orders_details, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    public void minusPersonnel(Member member) {
        this.personnel -= 1;
        this.modifiedBy = member;
    }

    public void plusPersonnel(Member member) {
        this.personnel += 1;
        this.modifiedBy = member;
    }

    public void changePositions(String positions) {
        this.positions = positions;
    }

    public void setExtraRun(Member modifiedBy) {
        this.isExtraRun = true;
        this.modifiedBy = modifiedBy;
    }

    public void setNotExtraRun(Member modifiedBy) {
        this.isExtraRun = false;
        this.modifiedBy = modifiedBy;
    }

}

