package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.SmallShipResponse;

import java.util.List;

public interface AdRepositoryCustom {
    List<SmallShipResponse> getAdByType(AdType type);
}
