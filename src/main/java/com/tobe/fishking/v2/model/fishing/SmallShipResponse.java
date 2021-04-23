package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
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
            Long shipId,
            String profileImage,
            String shipName,
            String sido,
            String sigungu,
            Location location,
            String address,
            FishingType type
    ) {
        this.id = shipId;
        this.shipImageFileUrl = "/resource/" + profileImage.split("/")[1] + "/thumb_" + profileImage.split("/")[2];
        this.shipName = shipName;
        this.sido = sido;
        this.sigungu = sigungu;
        this.location = location;
        this.address = address;
        this.lowPrice = lowPrice==null?0:lowPrice;
    }

    public void setSpecies(List<CommonCode> species) {
        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
        this.fishSpeciesCount = this.fishSpecies.size();
    }

}
