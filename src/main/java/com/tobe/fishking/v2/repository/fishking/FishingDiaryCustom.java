package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.model.board.FishingDiaryMainResponse;
import com.tobe.fishking.v2.model.board.FishingDiarySearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FishingDiaryCustom {
    List<FishingDiary> getDiaryByShipId(Long ship_id);
    List<FishingDiary> getBlogByShipId(Long ship_id);
    Page<FishingDiary> searchDiary(String keyword, Pageable pageable);
    Page<FishingDiarySearchResponse> searchDiaryOrBlog(String keyword, String type, Pageable pageable);
    List<FishingDiaryMainResponse> getMainDiaries();
}
