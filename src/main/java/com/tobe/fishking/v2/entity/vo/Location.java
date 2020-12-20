package com.tobe.fishking.v2.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Location {
    private Long longitude;
    private Long latitude;

    public Location(Long longitude, Long latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
