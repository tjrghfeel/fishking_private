package com.tobe.fishking.v2.entity.common;

import com.tobe.fishking.v2.entity.BaseTime;
import com.tobe.fishking.v2.model.common.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "observer_code")
public class ObserverCode extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY
    private Long id;

    @Column(columnDefinition = "varchar(7) comment '관측소 번호' ")
    private String code;

    @Column(columnDefinition = "varchar(30) comment '관측소 이름' ")
    private String name;

    private Location location;

    @Builder
    public ObserverCode(String code, Location location) {
        this.code = code;
        this.location = location;
    }

    public Double distanceFrom(Location location) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(location.getLatitude() - this.location.getLatitude());
        double lonDistance = Math.toRadians(location.getLongitude() - this.location.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.location.getLatitude())) * Math.cos(Math.toRadians(location.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        // 높이 같음
        // double height = el1 - el2;
        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
}
