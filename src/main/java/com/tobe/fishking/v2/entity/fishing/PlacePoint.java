package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.model.common.Location;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class PlacePoint extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false,nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = " comment  '갯바위'  ")
    private Places place;

    @Column(columnDefinition = "varchar(30)   comment '위치'  ")
    private Location location;

    @Column(columnDefinition = "varchar(30)   comment '번호'  ")
    private Integer number;

    // EXEC sp_addextendedproperty 'MS_Description', N'생성자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  created_by
    @ManyToOne
    @JoinColumn(name="created_by" ,   updatable= false , columnDefinition  = " bigint not null comment '생성자'")
    private Member createdBy;

    // EXEC sp_addextendedproperty 'MS_Description', N'수정자', 'USER', DBO, 'TABLE', goods, 'COLUMN',  modified_by
    @ManyToOne
    @JoinColumn(name="modified_by" ,  columnDefinition = "bigint NOT NULL   comment '수정자'  ")
    private Member modifiedBy;

    @Builder
    public PlacePoint(Places place, Location location, Integer number, Member member) {
        this.place = place;
        this.location = location;
        this.number = number;
        this.createdBy = member;
        this.modifiedBy = member;
    }
}
