package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.model.fishing.GoodsResponse;

import java.util.List;

public interface GoodsRepositoryCustom {
    List<GoodsResponse> getShipGoods(Long ship_id);
}
