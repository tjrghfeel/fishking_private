package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipRepositoryCustom {
    Page<Ship> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable);
}
