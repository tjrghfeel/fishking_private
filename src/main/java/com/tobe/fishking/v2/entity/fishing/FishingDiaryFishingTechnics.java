package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import lombok.Getter;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "fishing_diary_fishing_technics")
public class FishingDiaryFishingTechnics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fishing_diary_id")
    private FishingDiary fishingDiary;

    @Column
    @Enumerated(EnumType.STRING)
    private FishingTechnic fishingTechnic;


    public FishingDiaryFishingTechnics(FishingDiary fishingDiary, FishingTechnic fishingTechnic)
    {
        this.fishingDiary = fishingDiary;
        this.fishingTechnic = fishingTechnic;
    }
}
