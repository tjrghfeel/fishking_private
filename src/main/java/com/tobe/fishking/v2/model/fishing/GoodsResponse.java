package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.GoodsFishingDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class GoodsResponse {

    private Long id;
    private String name;
    private Integer price;
    private String startTime;
    private String endTime;
    private List<String> fishSpecies;
    private List<String> fishingDates;
    private String startFishingDates;
    private String endFishingDates;
    private Integer minPersonnel;
    private Integer maxPersonnel;
    private Integer reservationPersonal;
    private String observerCode;
    private List<String> positions;
    private List<String> usedPositions;
    private String shipMaxPersonnel;

    @Builder
    public GoodsResponse(Goods goods, Integer rideMember) {
        this.id = goods.getId();
        this.name = goods.getName();
        this.price = goods.getTotalAmount();
        this.startTime = goods.getFishingStartTime();
        this.endTime = goods.getFishingEndTime();
        this.fishSpecies = goods.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.fishingDates = goods.getFishingDates().stream().sorted(Comparator.comparing(GoodsFishingDate::getFishingDate)).map(GoodsFishingDate::getFishingDateString).collect(Collectors.toList());
        this.startFishingDates = this.fishingDates.get(0);
        this.endFishingDates = this.fishingDates.get(this.fishingDates.size()-1);
        this.minPersonnel = goods.getMinPersonnel();
        this.maxPersonnel = goods.getMaxPersonnel();
        this.reservationPersonal = rideMember == null ? 0 : rideMember;
        this.observerCode = goods.getShip().getObserverCode();
        this.shipMaxPersonnel = goods.getShip().getBoardingPerson().toString();
    }

    public GoodsResponse(Goods goods) {
        this.id = goods.getId();
        this.name = goods.getName();
        this.price = goods.getTotalAmount();
        this.startTime = goods.getFishingStartTime();
        this.endTime = goods.getFishingEndTime();
        this.fishSpecies = goods.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.fishingDates = goods.getFishingDates().stream().sorted(Comparator.comparing(GoodsFishingDate::getFishingDate)).map(GoodsFishingDate::getFishingDateString).collect(Collectors.toList());
        this.startFishingDates = this.fishingDates.get(0);
        this.endFishingDates = this.fishingDates.get(this.fishingDates.size()-1);
        this.minPersonnel = goods.getMinPersonnel();
        this.maxPersonnel = goods.getMaxPersonnel();
        this.reservationPersonal = 0;
        this.observerCode = goods.getShip().getObserverCode();
        this.shipMaxPersonnel = goods.getShip().getBoardingPerson().toString();
    }
}
