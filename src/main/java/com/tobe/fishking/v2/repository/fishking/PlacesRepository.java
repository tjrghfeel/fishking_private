package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.common.Points;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacesRepository extends BaseRepository<Places, Long> {

    @Query("select p from Places p \n" +
            " join p.location pi where pi.longitude is not null or pi.longitude is not null ")
    List<Places> findAllPlacesAndLocation();

    @Query("select r.places from ShipSeaRocks r where r.ship = :ship")
    List<Places> getPlacesByShip(Ship ship);

    @Query("select r.places from ShipSeaRocks r where r.ship.id = :shipId")
    List<Places> getPlacesByShipId(Long shipId);

}

