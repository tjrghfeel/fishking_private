package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FishingDiaryMainResponse {

    private Long id;
    private String title;
    private String imageUrl;
    private String sido;
    private String sigungu;
    private String species;

    public FishingDiaryMainResponse(FishingDiary diary) {
        this.id = diary.getId();
        this.title = diary.getTitle();
        this.sido = diary.getShip().getSido();
        this.sigungu = diary.getShip().getSigungu();
        this.species = diary.getFishingSpeciesName();
    }

}
