package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.exception.FileNotFoundException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.FilesRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShipDTO {

    private static FilesRepository fileRepo;
    private String shipImageFileUrl;

    private String shipName;
    private String sido;
    private String sigungu;
    private double distance;

    private List<CommonCode> fishSpecies = new ArrayList<>();

    private int fishSpeciesCount;

    private Company company;


    public static ShipDTO of(Ship ship){

        FileEntity shipFile = fileRepo.findById(ship.getId())
                .orElseThrow(FileNotFoundException::new);

        return ShipDTO.builder()
                .shipName(ship.getShipName())
                .sido(ship.getShipName())
                .sigungu(ship.getSigungu())
                .distance(ship.getDistance())
                .fishSpecies(ship.getFishSpecies())
                .fishSpeciesCount(ship.getFishSpecies().size())
                .company(ship.getCompany())
                .shipImageFileUrl(shipFile.getDownloadUrl())
                .build();
    }
}