package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShipRepositoryCustom {
    Page<ShipListResponse> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable);
}
