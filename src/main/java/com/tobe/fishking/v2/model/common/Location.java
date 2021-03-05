package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.utils.CalcUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;


@Embeddable
@NoArgsConstructor
@Getter
public class Location {

    private Double latitude;
    private Double longitude;

    @Builder
    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getDistance(Double lat, Double lon) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat - this.latitude);
        double lonDistance = Math.toRadians(lon - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        // 높이 같음
        // double height = el1 - el2;
        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return CalcUtils.round((Math.sqrt(distance) / 1000), 3);
    }
}
