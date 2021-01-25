package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.fishing.FishSpecies;
import com.tobe.fishking.v2.enums.fishing.FishingLure;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import com.tobe.fishking.v2.exception.FileNotFoundException;
import com.tobe.fishking.v2.model.ModelMapperUtils;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.repository.common.FileRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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




        @ApiModelProperty(name = "어종")
        @Builder.Default
        private List<CommonCode> fishSpecies = new ArrayList<>();
/*
        private List<ShipFishSpecies> shipFishSpecies;
*/

        private int fishSpeciesCount;

       // private Company company;


        public static ShipDTO.ShipDTOResp of(Ship ship){
            return ShipDTOResp.builder()
                    .id(ship.getId())
                    .shipName(ship.getShipName())
                    .sido(ship.getShipName())
                    .sigungu(ship.getSigungu())
                    .distance(ship.getDistance())
                    .location(ship.getLiveLocation()== null?ship.getLocation():ship.getLiveLocation())
                    .address(ship.getAddress())
                    .fishSpecies(ship.getFishSpecies())
                    .fishSpeciesCount(ship.getFishSpecies().size())
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