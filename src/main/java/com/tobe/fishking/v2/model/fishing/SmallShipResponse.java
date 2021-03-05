package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.common.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Stream<CommonCode> species = ship.getFishSpecies().stream().filter(CommonCode::getIsActive);
        this.id = ship.getId();
        this.shipImageFileUrl = "/resource" + ship.getProfileImage();
        this.shipName = ship.getShipName();
        this.sido = ship.getSido();
        this.sigungu = ship.getSigungu();
        this.location = ship.getLocation();
        this.address = ship.getAddress();
        this.fishSpecies = species.map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
        this.fishSpeciesCount = this.fishSpecies.size();
        this.lowPrice = lowPrice==null?0:lowPrice;
    }


}
