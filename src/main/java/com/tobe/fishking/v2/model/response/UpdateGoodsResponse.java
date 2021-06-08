package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.GoodsFishingDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UpdateGoodsResponse {

    private Long shipId;
    private String shipName;
    private String name;
    private Integer amount;
    private Integer minPersonnel;
    private Integer maxPersonnel;
    private String fishingStartTime;
    private String fishingEndTime;
    private Boolean isUse;
    private List<String> species;
    private List<String> fishingDates;
    private String reserveType;
    private Boolean positionSelect;
    private Boolean extraRun;
    private Integer extraPersonnel;
    private Integer extraShipNumber;

    @QueryProjection
    public UpdateGoodsResponse(Goods goods, Long shipId, String shipName) {
        this.shipId = shipId;
        this.shipName = shipName;
        this.name = goods.getName();
        this.amount = goods.getTotalAmount();
        this.minPersonnel = goods.getMinPersonnel();
        this.maxPersonnel = goods.getMaxPersonnel();
        this.fishingStartTime = goods.getFishingStartTime();
        this.fishingEndTime = goods.getFishingEndDate() + goods.getFishingEndTime();
        this.isUse = goods.getIsUse();
        this.species = goods.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.fishingDates = goods.getFishingDates().stream().map(GoodsFishingDate::getFishingDateString).collect(Collectors.toList());
        this.reserveType = goods.getReserveType().getKey();
        this.positionSelect = goods.getPositionSelect();
        this.extraRun = goods.getExtraRun();
        this.extraPersonnel = goods.getExtraPersonnel();
        this.extraShipNumber = goods.getExtraShipNumber();
    }
}
