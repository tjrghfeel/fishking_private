package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipRepositoryCustom {
    Page<ShipListResponse> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable);
    Page<ShipListResponse> searchMain(String keyword, String type, Double lat, Double lng, Pageable pageable);
    Page<ShipListResponse> searchMainWithType(String keyword, String type, Double lat, Double lng, Pageable pageable);
    List<ShipListResponse> searchAllForMap(ShipSearchDTO shipSearchDTO);
    Page<TvListResponse> searchTvList(ShipSearchDTO shipSearchDTO, Pageable pageable);
    ShipResponse getDetail(Long ship_id);
    Page<FishingShipResponse> getShipsByCompanyMember(Long memberId, String keywordType, String keyword, String status, Pageable pageable);
    Page<FishingShipResponse> getShipsByCompanyMember2(Long memberId, String keyword, String cameraActive, Pageable pageable);
}
