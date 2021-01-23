package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import com.tobe.fishking.v2.model.ModelMapperUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;


@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsDTO {

    @Getter
    @Setter
    public static class Create {

   /*     private String content;

        @Builder
        public Create(String content) {
         ...
        }*/
    }

    @Getter
    @Setter
    public static class Update {

     //   private Long   id;
     //   private String content;
     //     ...


    }

    @Getter
    @Setter
    public static class GoodsDTOResp {

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
        private boolean isBegginerPossible;
        private String shipStartTime;
        private Meridiem meridiem;
        private boolean isClose;
        private Ship ship;

/*    public static GoodsDTOResp of(Goods goods){
        return GoodsDTOResp.builder()
                .goodsId(goods.getId())
                .goodsName(goods.getName())
                .totalAmount(goods.getTotalAmount())
                .isBegginerPossible(goods.getIsBeginnerPossible())
                .shipStartTime(goods.getShipStartTime())
                .meridiem(goods.getMeridiem())
                .isClose(goods.getIsClose())
                .build();
    }*/

        // Entity -> DTO
        public static com.tobe.fishking.v2.model.fishing.GoodsDTO.GoodsDTOResp of(Goods goods) {
            return ModelMapperUtils.map(goods, com.tobe.fishking.v2.model.fishing.GoodsDTO.GoodsDTOResp.class);
        }

        // Entity -> DTO (Page의 경우)
        public static Page<com.tobe.fishking.v2.model.fishing.GoodsDTO.GoodsDTOResp> of(Page<Goods> sourcePage) {
            return sourcePage.map(com.tobe.fishking.v2.model.fishing.GoodsDTO.GoodsDTOResp::of);
        }



    }


}
