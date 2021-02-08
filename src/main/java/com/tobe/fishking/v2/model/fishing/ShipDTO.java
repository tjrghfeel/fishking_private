package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
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

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShipDTO {


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
    @Builder
    public static class ShipDTOResp {



        private Long   id ;
        private String shipImageFileUrl;
        private String shipName;
        private String sido;
        private String sigungu;
        private double distance;

        private Location location;
        private String address;




//        @ApiModelProperty(name = "어종")
//        @Builder.Default
//        private List<CommonCode> fishSpecies = new ArrayList<>();

        @ApiModelProperty(name = "어종")
        @Builder.Default
        private List<CommonCodeDTO> fishSpecies = new ArrayList<>();
/*
        private List<ShipFishSpecies> shipFishSpecies;
*/

        private int fishSpeciesCount;

       // private Company company;


//        public static ShipDTO.ShipDTOResp ofOld(Ship ship){
//            return ShipDTOResp.builder()
//                    .id(ship.getId())
//                    .shipName(ship.getShipName())
//                    .sido(ship.getShipName())
//                    .sigungu(ship.getSigungu())
//                    .distance(ship.getDistance())
//                    .location(ship.getLiveLocation()== null?ship.getLocation():ship.getLiveLocation())
//                    .address(ship.getAddress())
//                    .fishSpecies(ship.getFishSpecies())
//                    .fishSpeciesCount(ship.getFishSpecies().size())
//         //           .company(ship.getCompany())
//                    .build();
//        }

        public static ShipDTO.ShipDTOResp of(Ship ship){
            return ShipDTOResp.builder()
                    .id(ship.getId())
                    .shipName(ship.getShipName())
                    .sido(ship.getSido())
                    .sigungu(ship.getSigungu())
                    .distance(ship.getDistance())
                    .location(ship.getLiveLocation()== null?ship.getLocation():ship.getLiveLocation())
                    .address(ship.getAddress())
                    .fishSpecies(ship.getFishSpecies().stream().map(CommonCodeDTO::fromEntity).collect(Collectors.toList()))
                    .fishSpeciesCount(ship.getFishSpecies().size())
                    .build();
        }



/*        // Entity -> DTO
        public static com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp of(Ship ship) {

            return ModelMapperUtils.map(ship, com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp.class);
        }
~
        // Entity -> DTO (Page의 경우)
        public static Page<ShipDTO.ShipDTOResp> of(Page<Ship> sourcePage) {
            return sourcePage.map(com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp::of);
        }


        // Entity -> DTO (List의 경우)
        public static List<ShipDTO.ShipDTOResp> of(List<Ship> sourceList) {
            return sourceList.stream().map(com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp::of).collect(Collectors.toList());
        }*/

    }




}