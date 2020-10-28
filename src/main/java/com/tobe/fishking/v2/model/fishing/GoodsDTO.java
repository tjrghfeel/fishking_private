package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.fishing.Goods;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsDTO {
    private Long goodsId;
    private String goodsName;

    public static GoodsDTO of(Goods goods){
        return GoodsDTO.builder()
                .goodsId(goods.getId())
                .goodsName(goods.getName())
                .build();
    }
}
