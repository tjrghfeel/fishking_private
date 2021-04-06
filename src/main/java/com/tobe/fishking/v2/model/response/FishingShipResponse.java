package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class FishingShipResponse {

    private Long id;
    private String shipName;
    private String profileImage;
    private String address;
    private String fishingType;
    private String createDate;
    private Boolean hasCamera;
    private List<String> species;
    private Integer speciesCount;
    private List<GoodsSmallResponse> goodsList = null;

    @QueryProjection
    public FishingShipResponse(Ship ship) {
        this.id = ship.getId();
        this.shipName = ship.getShipName();
        this.profileImage = "/resource/" + ship.getProfileImage().split("/")[1] + "/thumb_" + ship.getProfileImage().split("/")[2];
        this.address = ship.getAddress();
        this.fishingType = ship.getFishingType().getValue();
        this.createDate = DateUtils.getDateTimeInFormat(ship.getCreatedDate());
        if (ship.getShiipRealTimeVideos().size() == 0) {
            this.hasCamera = false;
        } else {
            this.hasCamera = ship.getShiipRealTimeVideos().stream().anyMatch(RealTimeVideo::getIsUse);
        }
        this.species = ship.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.speciesCount = this.species.size();
    }

}
