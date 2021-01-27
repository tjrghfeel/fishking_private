package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.common.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    private double distance;
    private Location location;
    private String address;
    private List<CommonCodeDTO> fishSpecies;
    private int fishSpeciesCount;
    private int lowPrice;
    private int sold;

    public ShipListResponse(
            Long   id ,
            String shipName,
            String sido,
            String sigungu,
            double distance,
            Location location,
            String address,
            Integer lowPrice,
            Integer sold
//            List<CommonCode> fishSpecies
    ) {
        this.id = id;
        this.shipImageFileUrl = "";
        this.shipName = shipName;
        this.sido = sido;
        this.sigungu = sigungu;
        this.distance = distance;
        this.location = location;
        this.address = address;
//        this.fishSpecies = fishSpecies.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
//        this.fishSpeciesCount = fishSpecies.size();
        this.lowPrice = lowPrice==null?0:lowPrice;
        this.sold = sold==null?0:sold;
    }
}
