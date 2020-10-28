package com.tobe.fishking.v2.repository.fishing;


import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends BaseRepository<Goods, Long> {

    @Query("select g.fishSpecies, count(g.id) as cnt from Goods g group by g.fishSpecies")
    List<Object[]> countTotalGoodsByFishSpecies();

    //@Query("select s.byRegion, count(g.id) as cnt from Goods as g inner join Ship as s on s.id  = g.ship.id  WHERE g.fishingDate = :fishingDate  group by s.byRegion")
    //List<Object[]> countTotalGoodsByRegion( @Param("fishingYm") String fishingYM);
    //lower(n.title) LIKE %?#{[0].toLowerCase()}%")

    @Query("select s.byRegion, count(g.id) as cnt from Goods as g inner join Ship as s on s.id  = g.ship.id   group by s.byRegion")
    List<Object[]> countTotalGoodsByRegion();


}
