package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrdersInfoDTO {



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
    @AllArgsConstructor
    @Builder
    public static class ShipByOrdersDTOResp {


        private Long shipId;
        private Long goodsId;
        private String shipName;
        private String captain;
        private Long ordersPersonal;
        private Long ridePersonnel;
        private Long boardingPerson;

        private boolean shipDapartStatus;

        private Location location;


        public static OrdersInfoDTO.ShipByOrdersDTOResp of(OrderDetails orderDtails) {
            return ShipByOrdersDTOResp.builder()
                    .shipId(orderDtails.getGoods().getShip().getId())
                    .goodsId(orderDtails.getGoods().getId())
                    .shipName(orderDtails.getGoods().getShip().getShipName())
                    .captain(orderDtails.getGoods().getShip().getCaptain())
                    .ordersPersonal(orderDtails.getPersonnel().longValue())
                    .ridePersonnel(orderDtails.getRidePersonnel().longValue())
                    .boardingPerson(orderDtails.getGoods().getShip().getBoardingPerson().longValue())
                    .shipDapartStatus(orderDtails.getGoods().getShip().isDepartStatus())
                    .location(orderDtails.getGoods().getShip().getLiveLocation())
                    .build();
        }

    }







}
