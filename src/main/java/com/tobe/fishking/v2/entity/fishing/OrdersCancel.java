package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor

@Table(name = "orders_cancel")
public class OrdersCancel {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
// @Column(columnDefinition = "comment 'id'  ")
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'주문일자', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN',  order_date
    @Column(columnDefinition = "varchar(8) not null   comment '주문일자'  ")
    private String orderCancelDate;

    // EXEC sp_addextendedproperty 'MS_Description', N'오더', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN', orders
    @ManyToOne
    @JoinColumn(name = "order_cancel_orders_id", columnDefinition = "bigint  not null   comment '오더'  ")
    private Orders orders;


    @ManyToMany(targetEntity= CommonCode.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "cancel_reason ",  columnDefinition = "comment '취소사유코드'  ")  //postion_on_ship
    @Builder.Default
    private List<CommonCode> orderCancelReasons = new ArrayList<>();

    // EXEC sp_addextendedproperty 'MS_Description', N'기타취소사유', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN',  cancel_etc_reason
    @Column(columnDefinition = "varchar(200) not null   comment '기타취소사유'  ")
    private String cancelEtcReason;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', orders_cancel, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;



}
