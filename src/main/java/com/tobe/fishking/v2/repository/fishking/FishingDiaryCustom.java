package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;

import java.util.List;

public interface FishingDiaryCustom {
    List<FishingDiary> getDiaryByShipId(Long ship_id);
    List<FishingDiary> getBlogByShipId(Long ship_id);
}
