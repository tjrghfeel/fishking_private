package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.entity.fishing.ShipSeaRocks;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipSeaRocksRepository extends BaseRepository<ShipSeaRocks, Long> {
    @Query("select r from ShipSeaRocks r where r.ship = :ship and r.places = :places")
    ShipSeaRocks getShipSeaRocksByPlacesAndShip(Places places, Ship ship);
}
