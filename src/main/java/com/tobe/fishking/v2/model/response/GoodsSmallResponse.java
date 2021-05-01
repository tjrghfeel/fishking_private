package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.ReserveType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoodsSmallResponse {

    private Long id;
    private String name;
    private String fishingStartTime;
    private Integer minPersonnel;
    private Integer maxPersonnel;
    private Integer amount;
    private String status;
    private String select;
    private String confirm;
    private String extra;

    @QueryProjection
    public GoodsSmallResponse(Long id,
                              String name,
                              String fishingStartTime,
                              Integer minPersonnel,
                              Integer maxPersonnel,
                              Integer amount,
                              Boolean status,
                              Boolean select,
                              ReserveType confirm,
                              Boolean add) {
        this.id = id;
        this.name = name;
        this.fishingStartTime = fishingStartTime;
        this.minPersonnel = minPersonnel;
        this.maxPersonnel = maxPersonnel;
        this.amount = amount;
        this.status = status ? "판매" : "판매중지";
        this.select = select ? "ON" : "OFF";
        this.confirm = confirm.getValue();
        this.extra = add ? "ON" : "OFF";
    }

}
