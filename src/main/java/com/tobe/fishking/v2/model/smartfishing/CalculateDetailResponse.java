package com.tobe.fishking.v2.model.smartfishing;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalculateDetailResponse {

    private String payDate;
    private String orderName;
    private String goodsName;
    private Integer personnel;
    private Integer payAmount;

    @QueryProjection
    public CalculateDetailResponse(String payDate, String orderName, String goodsName, Integer personnel, Long payAmount) {
        this.payDate = payDate;
        this.orderName = orderName;
        this.goodsName = goodsName;
        this.personnel = personnel;
        this.payAmount = payAmount.intValue();
    }
}
