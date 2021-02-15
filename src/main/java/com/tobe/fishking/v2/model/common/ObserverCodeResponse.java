package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.common.ObserverCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObserverCodeResponse {

    private Long id;
    private String code;
    private String name;
    private Double latitude;
    private Double longitude;
    private String seaDirection;
    private Integer xgrid;
    private Integer ygrid;

    public ObserverCodeResponse(ObserverCode code) {
        this.id = code.getId();
        this.code = code.getCode();
        this.name = code.getName();
        this.latitude = code.getLocation().getLatitude();
        this.longitude = code.getLocation().getLongitude();
        this.seaDirection = code.getSeaDirection().getValue();
        this.xgrid = code.getXGrid();
        this.ygrid = code.getYGrid();
    }
}
