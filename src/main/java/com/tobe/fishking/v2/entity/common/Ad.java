package com.tobe.fishking.v2.entity.common;


import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.common.AdType;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "ad") //ad
// EXEC sp_addextendedproperty 'MS_Description', N'광고', 'USER', DBO, 'TABLE', ad
public class Ad extends BaseTime {

        // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ad, 'COLUMN',  id
        //mssql 주석 >  EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', board, 'COLUMN',  id

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY)
        @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
        private Long Id;

        // EXEC sp_addextendedproperty 'MS_Description', N'광고타입', 'USER', DBO, 'TABLE', ad, 'COLUMN',  adType
        @Column(columnDefinition = "varchar(50)  comment '광고타압명'  ")
        @Enumerated(EnumType.ORDINAL)
        private AdType adType;

        // EXEC sp_addextendedproperty 'MS_Description', N'상품정보', 'USER', DBO, 'TABLE', ad, 'COLUMN',  order_goods
        @ManyToOne
        @JoinColumn(name = "order_goods_id", columnDefinition = "bigint not null   comment '상품정보'  ")
        private Goods goods;

        // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-시작', 'USER', DBO, 'TABLE', ad, 'COLUMN',  exposure_Start_Dt
        @Column(columnDefinition = "varchar(8)   comment '노출기간-시작'  ")
        private String exposureStartDt;

        // EXEC sp_addextendedproperty 'MS_Description', N'노출기간-시작', 'USER', DBO, 'TABLE', ad, 'COLUMN',  exposure_End_dt
        @Column(columnDefinition = "varchar(8)   comment '노출기간-종료'  ")
        private String exposureEndDt;

        // EXEC sp_addextendedproperty 'MS_Description', N'비고', 'USER', DBO, 'TABLE', ad, 'COLUMN',  remark
        @Column(columnDefinition = "varchar(500)   comment '비고'  ")
        private String remark;


        // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ad, 'COLUMN',  created_by
        @ManyToOne
        @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint  NOT NULL   comment '생성자'  ")
        private Member createdBy;

        // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ad, 'COLUMN',  modified_by
        @ManyToOne
        @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
        private Member modifiedBy;

}
