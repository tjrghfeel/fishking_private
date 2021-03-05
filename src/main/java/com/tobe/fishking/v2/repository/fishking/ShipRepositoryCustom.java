package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipRepositoryCustom {
    Page<ShipListResponse> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable);
    Page<ShipListResponse> searchMain(String keyword, String type, Double lat, Double lng, Pageable pageable);
    Page<ShipListResponse> searchMainWithType(String keyword, String type, Double lat, Double lng, Pageable pageable);
    List<ShipListResponse> searchAllForMap(ShipSearchDTO shipSearchDTO);
    ShipResponse getDetail(Long ship_id);
}
