package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
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

    @Query("select p from Places p " +
            "where p.sido like concat('%', :sido, '%') and p.sigungu like concat('%', :sigungu, '%') and p.dong like concat('%', :dong, '%') " +
            "and (p.open = true or p.createdBy = :member)")
    List<Places> getPlacesByAddress(String sido, String sigungu, String dong, Member member);

    @Query("select p from Places p where p.id in :ids")
    List<Places> getPlacesInId(Long[] ids);

}

