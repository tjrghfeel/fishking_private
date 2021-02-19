package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.common.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class SmallShipResponse {

    private Long   id;
    private String shipImageFileUrl;
    private String shipName;
    private String sido;
    private String sigungu;
    private double distance;
    private Location location;
    private String address;
    private List<CommonCodeDTO> fishSpecies;
    private int fishSpeciesCount;
    private int lowPrice;

    public SmallShipResponse(
            Integer lowPrice,
            Ship ship
    ) {
        List<CommonCode> species = ship.getFishSpecies();
        this.id = ship.getId();
        this.shipImageFileUrl = "";
        this.shipName = ship.getShipName();
        this.sido = ship.getSido();
        this.sigungu = ship.getSigungu();
        this.location = ship.getLocation();
        this.address = ship.getAddress();
        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
        this.fishSpeciesCount = species.size();
        this.lowPrice = lowPrice==null?0:lowPrice;
    }


}
