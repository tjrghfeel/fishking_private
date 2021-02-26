package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Points;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.model.common.Location;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PlacesDTO {


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
    public static class PlacesDTOResp {



        private Long   id ;
        private String placesImageFileUrl;
        private String placeName;
        private String sido;
        private String sigungu;
        private double distance;

        private Location location;
        private String address;

        @ApiModelProperty(name = "어종")
        @Builder.Default
        private List<CommonCode> fishSpecies = new ArrayList<>();
/*
        private List<ShipFishSpecies> shipFishSpecies;
*/

        private int fishSpeciesCount;

       // private Company company;


        public static PlacesDTO.PlacesDTOResp of(Places place){
            return PlacesDTOResp.builder()
                    .id(place.getId())
                    .sido(place.getSido())
                    .sigungu(place.getSigungu())
                    //.distance(place.getDistance())
                    .location(place.getLocation())
                    .address(place.getAddress())
                  //  .fishSpecies(place.getFishSpecies())
                  //  .fishSpeciesCount(place.getFishSpecies().size())
         //           .company(ship.getCompany())
                    .build();
        }




/*        // Entity -> DTO
        public static com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp of(Ship ship) {

            return ModelMapperUtils.map(ship, com.tobe.fishking.v2.model.fishing.ShipDTO.ShipDTOResp.class);
        }

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