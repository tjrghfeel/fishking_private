package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.AccumuateType;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor

@Table(name = "points")
public class Points {


    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', points, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
// @Column(columnDefinition = "comment 'id'  ")
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    //결제수단 , //결제방법
    // EXEC sp_addextendedproperty 'MS_Description', N'결제수단', 'USER', DBO, 'TABLE', points, 'COLUMN',  pay_method
    @Column(columnDefinition = "int comment '결제수단'  ")
    @Enumerated(EnumType.ORDINAL)
    private AccumuateType accumuate;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성일자', 'USER', DBO, 'TABLE', points, 'COLUMN',  id
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "varchar(10)   comment '최초생성일자'  ")
    private String makeData;



    // EXEC sp_addextendedproperty 'MS_Description', N'포인트', 'USER', DBO, 'TABLE', points, 'COLUMN',  title
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(columnDefinition = "float   comment '포인트'")
    private double point;



    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', points, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,    updatable= false , columnDefinition  = "bigint NOT NULL   comment '생성자'  ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', points, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null  comment '수정자'")
    private Member modifiedBy;

}
