package com.tobe.fishking.v2.model.smartfishing;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalculateResponse {

    private Long shipId;
    private String shipName;
    private Long total;
    private Long order;
    private Long cancel;

    @QueryProjection
    public CalculateResponse(Long id, String name, Long total, Long cancel) {
        this.shipId = id;
        this.shipName = name;
        this.total = total;
        this.order = total - cancel;
        this.cancel = cancel;
    }

}
