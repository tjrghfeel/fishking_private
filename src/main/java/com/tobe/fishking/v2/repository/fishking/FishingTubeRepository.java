package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingTube;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FishingTubeRepository extends BaseRepository<FishingTube, Long> {

    Page<FishingTube> findAll(Pageable pageable);

    List<FishingTube> findByVideoId(String videoId);

}
