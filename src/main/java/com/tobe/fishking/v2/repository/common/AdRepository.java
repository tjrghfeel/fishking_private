package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.Ad;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.model.admin.AdDto;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends BaseRepository<Ad, Long>, AdRepositoryCustom {

//    @Query("select a from Ad a where a.adType = :adType")
//    List<Ad> getAdByType(AdType adType);

    Ad findByShipAndAdType(Ship ship, AdType adType);

    @Query("select new com.tobe.fishking.v2.model.admin.AdDto(a, s) from Ad a join Ship s on a.ship = s order by a.adType")
    List<AdDto> findAllOrderByAdType();
}
