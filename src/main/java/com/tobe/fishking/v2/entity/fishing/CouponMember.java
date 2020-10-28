package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "Coupon_member")
@Table(name = "coupon_member") //coupon
public class CouponMember extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    public Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_coupon_id" , columnDefinition = "bigint comment  '쿠폰ID'")
    private Coupon coupon;

    @Column(columnDefinition = "varchar(50) comment  '쿠폰코드'")
    private String couponCode;


    //발급된후 Member가 직접 쿠폰번호를 등록할수 있다.
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="coupon_member_id"  , columnDefinition = "bigint comment  '사용자ID'")
    private Member member;

    // EXEC sp_addextendedproperty 'MS_Description', N'사용여부', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  is_use
    @Column(columnDefinition = "bit default 0  comment '사용여부'  ")
    private boolean isUse;

    @Column(columnDefinition = "datetime comment  '등록일시'")
    private LocalDateTime regDate;


    // EXEC sp_addextendedproperty 'MS_Description', N'사용일시', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  effective_days
    @Column(columnDefinition = "datetime comment  '사용일시'")
    private LocalDateTime useDate;

    // EXEC sp_addextendedproperty 'MS_Description', N'사용오더', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  orders_id
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="coupon_orders_id" , columnDefinition = "bigint comment  '사용오더'")
    private Orders ordes;

   //coupon 알림 생성 - 프로시져 push, 1) 1:1, 2)쿠폰, 3)새글, 4)예약


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', coupon_member, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


}
