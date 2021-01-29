package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.GoodsFishingDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class GoodsResponse {

    private String name;
    private Integer price;
    private String startTime;
    private String endTime;
    private List<String> fishSpecies;
    private List<String> fishingDates;

    @Builder
    public GoodsResponse(Goods goods) {
        this.name = goods.getName();
        this.price = goods.getTotalAmount();
        this.startTime = goods.getFishingStartTime();
        this.endTime = goods.getFishingEndTime();
        this.fishSpecies = goods.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.fishingDates = goods.getFishingDates().stream().sorted(Comparator.comparing(GoodsFishingDate::getFishingDate)).map(GoodsFishingDate::getFishingDateString).collect(Collectors.toList());
    }
}
