package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.utils.CalcUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShipListResponse {

    private Long   id;
    private String shipImageFileUrl;
    private String shipName;
    private String sido;
    private String sigungu;
    private int distance;
    private Location location;
    private String address;
    private List<CommonCodeDTO> fishSpecies;
    private int fishSpeciesCount;
    private int lowPrice;
    private int sold;
    private String type;

    public ShipListResponse(
            Integer lowPrice,
            Integer sold,
            Long liked,
            Double distance,
            Ship ship
    ) {
        List<CommonCode> species = ship.getFishSpecies();
        this.id = ship.getId();
        this.shipImageFileUrl = "/resource" + ship.getProfileImage();
        this.shipName = ship.getShipName();
        this.sido = ship.getSido();
        this.sigungu = ship.getSigungu();
//        this.distance = ship.getDistance();
        this.location = ship.getLocation();
        this.address = ship.getAddress();
        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
        this.fishSpeciesCount = species.size();
        this.lowPrice = lowPrice==null?0:lowPrice;
        this.sold = sold==null?0:sold;
        this.type = ship.getFishingType().getValue();
//        this.distance = CalcUtils.round(distance, 3);
        this.distance = (int) Math.round(distance);
    }
}
