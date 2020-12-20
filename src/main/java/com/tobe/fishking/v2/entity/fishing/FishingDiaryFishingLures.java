package com.tobe.fishking.v2.entity.fishing;

import com.tobe.fishking.v2.enums.fishing.FishingLure;
import lombok.Getter;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "fishing_diary_fishing_lures")
public class FishingDiaryFishingLures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fishing_diary_id")
    private FishingDiary fishingDiary;

    @Column
    @Enumerated(EnumType.STRING)
    private FishingLure fishingLure;


    public FishingDiaryFishingLures(FishingDiary fishingDiary, FishingLure fishingLure)
    {
        this.fishingDiary = fishingDiary;
        this.fishingLure = fishingLure;
    }
}
