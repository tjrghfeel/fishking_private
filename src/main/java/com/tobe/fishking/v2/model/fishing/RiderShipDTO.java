package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.common.Location;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RiderShipDTO {


    @Getter
    @Setter
    @Builder
    public static class RiderShipDTOResp {

        private Integer personnelCountByOrder;
        private Long riderCountByOrder;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class BoardingListByOrdersDTOResp {


        private Long orderDtailsId;
        private Long rideShipId;
        private String shipPassengerName;
        private String gender;  //남, 여
        private String birthDay;  //yy.mm.dd∂
        private String residenceCity;
        private String residenceGungu;
        private String phoneNumber;
        private boolean bFingerPrint;


        public static RiderShipDTO.BoardingListByOrdersDTOResp of(RideShip rideShip) {
            return BoardingListByOrdersDTOResp.builder()
                    .orderDtailsId(rideShip.getOrdersDetail().getId())
                    .rideShipId(rideShip.getId())
                    .shipPassengerName(rideShip.getName())
                    .gender(rideShip.getSex())
                    .birthDay(rideShip.getBirthday())
                    .residenceCity(rideShip.getResidenceCity())
                    .residenceGungu(rideShip.getResidenceGungu())
                    .phoneNumber(rideShip.getPhoneNumber())
                    .bFingerPrint(rideShip.isBFingerPrint())
                    .build();
        }

    }

}
