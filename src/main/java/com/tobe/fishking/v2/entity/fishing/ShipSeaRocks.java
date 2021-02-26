package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.entity.BaseTime;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class ShipSeaRocks extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) // IDENTITY //mssql
    @Column(updatable=false, nullable=false ,columnDefinition = "bigint  comment 'id' ")
    private Long id;

    @ManyToOne
    @JoinColumn(columnDefinition = "comment '선박'  ")
    private Ship ship;

    @ManyToOne
    @JoinColumn(columnDefinition = "comment '갯바위'  ")
    private Places places;

    @Builder
    public ShipSeaRocks(Ship ship, Places places) {
        this.ship = ship;
        this.places = places;
    }

    public void changeSeaRocks(Places places) {
        this.places = places;
    }

}
