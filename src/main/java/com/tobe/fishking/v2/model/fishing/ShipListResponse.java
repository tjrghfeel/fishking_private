package com.tobe.fishking.v2.model.fishing;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
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
            Ship ship,
            List<CommonCode> speciesList
    ) {
        List<CommonCode> species = ship.getFishSpecies();
        this.id = ship.getId();
        this.shipImageFileUrl = "/resource/" + ship.getProfileImage().split("/")[1] + "/thumb_" + ship.getProfileImage().split("/")[2];
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

//    @QueryProjection
//    public ShipListResponse(
//            Integer lowPrice,
//            Long sold,
//            Long liked,
//            Ship ship
//    ) {
//        List<CommonCode> species = ship.getFishSpecies();
//        this.id = ship.getId();
//        this.shipImageFileUrl = "/resource" + ship.getProfileImage();
//        this.shipName = ship.getShipName();
//        this.sido = ship.getSido();
//        this.sigungu = ship.getSigungu();
////        this.distance = ship.getDistance();
//        this.location = ship.getLocation();
//        this.address = ship.getAddress();
//        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
//        this.fishSpeciesCount = species.size();
//        this.lowPrice = lowPrice==null?0:lowPrice;
//        this.sold = sold==null?0:sold.intValue();
//        this.type = ship.getFishingType().getValue();
////        this.distance = CalcUtils.round(distance, 3);
////        this.distance = (int) Math.round(distance);
//    }
//
//    @QueryProjection
//    public ShipListResponse(
//            Integer lowPrice,
//            Long sold,
//            Long liked,
//            Double distance,
//            Ship ship,
//            List<CommonCode> speciesList
//    ) {
//        List<CommonCode> species = ship.getFishSpecies();
//        this.id = ship.getId();
//        this.shipImageFileUrl = "/resource/" + ship.getProfileImage().split("/")[1] + "/thumb_" + ship.getProfileImage().split("/")[2];
//        this.shipName = ship.getShipName();
//        this.sido = ship.getSido();
//        this.sigungu = ship.getSigungu();
////        this.distance = ship.getDistance();
//        this.location = ship.getLocation();
//        this.address = ship.getAddress();
//        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
//        this.fishSpeciesCount = species.size();
//        this.lowPrice = lowPrice==null?0:lowPrice;
//        this.sold = sold==null?0:sold.intValue();
//        this.type = ship.getFishingType().getValue();
////        this.distance = CalcUtils.round(distance, 3);
//        this.distance = (int) Math.round(distance);
//    }

    @QueryProjection
    public ShipListResponse(
            Integer lowPrice,
            Long sold,
            Long liked,
            Double distance,
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
        this.sold = sold==null?0:sold.intValue();
        this.type = type.getValue();
        this.distance = (int) Math.round(distance);
    }

    @QueryProjection
    public ShipListResponse(
            Integer lowPrice,
            Long sold,
            Long liked,
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
        this.sold = sold==null?0:sold.intValue();
        this.type = type.getValue();
    }

    public void setSpecies(List<CommonCode> species) {
        this.fishSpecies = species.stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList());
        this.fishSpeciesCount = species.size();
    }
}
