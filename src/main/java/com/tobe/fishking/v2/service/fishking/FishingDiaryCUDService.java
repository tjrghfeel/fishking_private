package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
//import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoGroup;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FishingDiaryCUDService {

    private final FishingDiaryRepository fishingDiaryRepo;

    @Autowired
    public FishingDiaryCUDService(FishingDiaryRepository fishingDiaryRepo) {
        this.fishingDiaryRepo = fishingDiaryRepo;
    }


//    @Transactional
//    public Long saveFishingDiary(FishingDiaryDtoGroup.SaveRequestDto saveRequestDto) {
//        FishingDiary fishingDiary = saveRequestDto.toEntity();
//        FishingDiary result = fishingDiaryRepo.save(fishingDiary);
//        return result.getId();
//    }
}