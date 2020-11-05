package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsDTO {

    /* 상품아이디
       상품명
       금액
       초보가능여부
       출발시간
       오전.오후
       마감여부
    */

    private Long goodsId;
    private String goodsName;
    private double totalAmount;
    private boolean isbegginerPossible;
    private String shipStartTime;
    private Meridiem meridiem;
    private boolean isClose;

    public static GoodsDTO of(Goods goods){
        return GoodsDTO.builder()
                .goodsId(goods.getId())
                .goodsName(goods.getName())
                .totalAmount(goods.getTotalAmount())
                .isbegginerPossible(goods.isBegginerPossible())
                .shipStartTime(goods.getShipStartTime())
                .meridiem(goods.getMeridiem())
                .isClose(goods.isClose())

                .build();
    }
}
