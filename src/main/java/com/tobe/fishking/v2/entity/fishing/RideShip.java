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
@Table(name = "ride_ship")
public class RideShip extends BaseTime {

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
// @Column(columnDefinition = "comment 'id'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'오더상세', 'USER', DBO, 'TABLE', orders, 'COLUMN',  fishing_ships
    @ManyToOne
    @JoinColumn(name = "ride_ship_order_details_id", columnDefinition = "bigint not null   comment '조황정보'  ")
    private OrderDetails ordersDetail;


    // EXEC sp_addextendedproperty 'MS_Description', N'이름', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  name
    @Column(columnDefinition = "varchar(100)   comment '이름'  ")
    private String name;

    // EXEC sp_addextendedproperty 'MS_Description', N'성별', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  sex
    @Column(columnDefinition = "varchar(1)   comment '성별'  ")
    private String sex;

    // EXEC sp_addextendedproperty 'MS_Description', N'생년월일', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  birthday
    @Column(columnDefinition = "varchar(8)   comment '생년월일'  ")
    private String birthday;

    // EXEC sp_addextendedproperty 'MS_Description', N'거주지-시도', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  residence_city
    @Column(columnDefinition = "varchar(50)   comment '거주지-시도'  ")
    private String residenceCity;


    // EXEC sp_addextendedproperty 'MS_Description', N'거주지', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  residence_gungu
    @Column(columnDefinition = "varchar(50)   comment '거주지-군구'  ")
    private String residenceGungu;

    @Column(columnDefinition = "varchar(15)   comment '핸드폰번호'  ")
    private String phoneNumber;

    // EXEC sp_addextendedproperty 'MS_Description', N'승선여부', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  is_ride
    @Column(columnDefinition = "bit  default 0  comment '승선여부'  ")
    private boolean isRide;


    // EXEC sp_addextendedproperty 'MS_Description', N'지문', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  finger_print
    @Column(columnDefinition = "varchar(4000)    comment '지문'  ")
    private String fingerPrint;


    // EXEC sp_addextendedproperty 'MS_Description', N'지문', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  is_finger_print
    @Column(columnDefinition = "bit default 0   comment '지문확인'  ")
    private boolean bFingerPrint;


    // EXEC sp_addextendedproperty 'MS_Description', N'경찰청지문확인: 0:수집 1:전송 2:확인 9:전송실패', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  status_certified
    @Column(columnDefinition = "int default 0   comment '경찰청지문확인: 0:수집 1:전송 2:확인 9:전송실패 '  ")
    private int statusCertified;


    // EXEC sp_addextendedproperty 'MS_Description', N'에러사유', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  error_reason
    @Column(columnDefinition = "varchar(2000)   comment '에러사유 '  ")
    private String errorReason;


    // EXEC sp_addextendedproperty 'MS_Description', N'선상위치', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  error_reason
    @Column(columnDefinition = "varchar(5)   comment '선상위치 '  ")
    private String shipPoint;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = " bigint not null comment '생성자' ")
    private Member createdBy;


    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ride_ship, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Builder
    public RideShip(OrderDetails ordersDetail, String name, String birthday, String phoneNumber, Member member) {
        this.ordersDetail = ordersDetail;
        this.name = name;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.createdBy = member;
        this.modifiedBy = member;
    }

    public void updateFingerPrint(String fingerPrint, Member member) {
        this.fingerPrint = fingerPrint;
        this.modifiedBy = member;
    }

}
