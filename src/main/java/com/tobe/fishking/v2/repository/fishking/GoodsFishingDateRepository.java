package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.GoodsFishingDate;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsFishingDateRepository extends BaseRepository<GoodsFishingDate, Long> {

    @Query("select g from GoodsFishingDate g where g.goods.id = :goods and g.fishingDateString = :date")
    GoodsFishingDate findByGoodsIdAndDateString(Long goods, String date);
}
