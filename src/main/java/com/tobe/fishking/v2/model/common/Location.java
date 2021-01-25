package com.tobe.fishking.v2.model.common;

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
}
