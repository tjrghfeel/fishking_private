package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public GoodsSmallResponse(Long id,
                              String name,
                              String fishingStartTime,
                              Integer minPersonnel,
                              Integer maxPersonnel,
                              Integer amount,
                              Boolean status) {
        this.id = id;
        this.name = name;
        this.fishingStartTime = fishingStartTime;
        this.minPersonnel = minPersonnel;
        this.maxPersonnel = maxPersonnel;
        this.amount = amount;
        this.status = status ? "판매" : "판매중지";
    }

}
