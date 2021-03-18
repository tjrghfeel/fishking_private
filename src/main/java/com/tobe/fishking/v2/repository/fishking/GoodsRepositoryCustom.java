package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.model.response.GoodsSmallResponse;

import java.time.LocalDate;
import java.util.List;

public interface GoodsRepositoryCustom {
    List<GoodsResponse> getShipGoods(Long ship_id, LocalDate date);
    List<GoodsResponse> getShipGoods(Long ship_id);
    List<GoodsSmallResponse> searchGoods(Long shipId, String keyword, String status);
}
