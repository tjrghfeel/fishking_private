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
@Table(name = "ship_operating_day")
public class ShipOperatingDay extends BaseTime {  //선상 조업일

    // EXEC sp_addextendedproperty 'MS_Description', N'id', 'USER', DBO, 'TABLE', ship_operating_day, 'COLUMN',  id
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
@Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    // EXEC sp_addextendedproperty 'MS_Description', N'선상', 'USER', DBO, 'TABLE', ship_operating_day, 'COLUMN',  ship
    @ManyToOne
    @JoinColumn(columnDefinition = " NOT NULL   comment '선상'  ")
    private Ship ship;

    // EXEC sp_addextendedproperty 'MS_Description', N'조업일자', 'USER', DBO, 'TABLE', ship_operating_day, 'COLUMN',  operting_day
    @JoinColumn(columnDefinition = " NOT NULL   comment '조업일자'  ")
    private String opertingDay;  //yyyyMMdd


    // EXEC sp_addextendedproperty 'MS_Description', N'휴일유무', 'USER', DBO, 'TABLE',ship_operating_day, 'COLUMN',  is_holiday
    @JoinColumn(columnDefinition = " NOT NULL   comment '휴업일자'  ")
    private boolean isHoliday;  //

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', ship_operating_day, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" , insertable= false ,  updatable= false , columnDefinition = " bigint not null comment '생성자' ")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', ship_operating_day, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="modified_by" , insertable= false ,  updatable= false , columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;


}
