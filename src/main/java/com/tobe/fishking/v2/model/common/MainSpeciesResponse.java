package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainSpeciesResponse {

    public String code;
    public String codeName;
    public Long count;

    public MainSpeciesResponse(String code, String codeName, Long count) {
        this.code = code;
        this.codeName = codeName;
        this.count = count;
    }

    public MainSpeciesResponse(SeaDirection direction, Long count) {
        this.code = direction.getKey();
        this.codeName = direction.getValue();
        this.count = count;
    }
}
