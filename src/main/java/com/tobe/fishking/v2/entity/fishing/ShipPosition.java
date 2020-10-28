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
@Table(name = "ship_position") //선상낚시-선상위치
public class ShipPosition  extends BaseTime {

    @Id
    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  id
    //@Column(columnDefinition=" INT(11) NOT NULL COMMENT '0 for no action, 1 for executed, 2 for validated, 3 for aproved'  ")
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;


    // EXEC sp_addextendedproperty 'MS_Description', N'위치', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  goods
    @Column(columnDefinition = "int NOT NULL comment '위치'  ")
    private int position;

    /*
    // EXEC sp_addextendedproperty 'MS_Description', N'선상-위치', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  goods
    @ManyToMany(targetEntity= Ship.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "goods_position_ship_id", columnDefinition = "int NOT NULL comment '선상-포인트'  ")  //postion_on_ship
    private List<Ship> postionOnShip = new ArrayList<>();*/

    // EXEC sp_addextendedproperty 'MS_Description', N'선상-위치', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  goods
    @ManyToOne
    @JoinColumn(name = "goods_position_goods_id", columnDefinition = "bigint NOT NULL comment '선상-위치'  ")
    private Goods goods;


    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null comment '생성자' ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ship_point, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

}
