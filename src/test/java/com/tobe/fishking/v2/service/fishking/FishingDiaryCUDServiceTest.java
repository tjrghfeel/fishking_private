package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.IntegrationTest;
import com.tobe.fishking.v2.entity.vo.Location;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.*;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FishingDiaryCUDServiceTest extends IntegrationTest {

    @Autowired
    FishingDiaryCUDService fishingDiaryCUDService;

    @Test
    void saveFishingDiary() {

        //given
        Set<FishSpecies> fishSpecies = new LinkedHashSet<>();
        fishSpecies.add(FishSpecies.hairtail);
        fishSpecies.add(FishSpecies.mackerel);

        Set<FishingTechnic> fishingTechnics = new LinkedHashSet<>();
        fishingTechnics.add(FishingTechnic.LURE);
        fishingTechnics.add(FishingTechnic.CARGO);

        Set<FishingLure> fishingLures = new LinkedHashSet<>();
        fishingLures.add(FishingLure.AEJA);
        fishingLures.add(FishingLure.KRILL);

        FishingDiaryDtoGroup.SaveRequestDto saveRequestDto
                = FishingDiaryDtoGroup.SaveRequestDto.builder()
                .filePublish(FilePublish.fishingDaily)
                .title("test title")
                .contents("test contents")
                .tideTime(TideTime.five)
                .fishSpecies(fishSpecies)
                .fishingLures(fishingLures)
                .fishingTechnics(fishingTechnics)
                .fishingType(FishingType.ship)
                .goodsId(10L)
                .memberId(5L)
                .location(new Location(100L, 200L))
                .date(LocalDate.now())
                .boardId(73L)
                .build();

        Long id = fishingDiaryCUDService.saveFishingDiary(saveRequestDto);

        assertNotNull(id);
    }

}