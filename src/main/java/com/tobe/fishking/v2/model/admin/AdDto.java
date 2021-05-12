package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.entity.common.Ad;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.common.AdType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdDto {
    private Long id;
    private String adType;
    private String adTypeCode;
    private Long createdBy;
    private Long shipId;
    private String createdDate;
    private String shipName;
    private String goodsList;
    private String speciesList;

    public AdDto(Ad ad, Ship ship){
        this.id = ad.getId();
        this.adType = ad.getAdType().getValue();
        this.adTypeCode = ad.getAdType().getKey();
        this.createdBy = ad.getCreatedBy().getId();
        this.shipId = ship.getId();
        this.createdDate = ad.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.shipName = ship.getShipName();

        this.goodsList = "";
        for(Goods goods : ship.getGoods()){
            this.goodsList += goods.getName()+", ";
        }
        if(this.goodsList.lastIndexOf(",") != -1) {
            this.goodsList = this.goodsList.substring(0, this.goodsList.lastIndexOf(","));
        }

        this.speciesList = "";
        for(CommonCode species : ship.getFishSpecies()){
            this.speciesList += species.getCodeName()+", ";
        }
        if(this.speciesList.lastIndexOf(",") != -1) {
            this.speciesList = this.speciesList.substring(0, this.speciesList.lastIndexOf(","));
        }
    }

}
