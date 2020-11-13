package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoodsRepository extends BaseRepository<Goods, Long> {

    //--선상/갯바위 별 조회
    List<Goods> findAllByFishingType(FishingType fishingType);

    //추천업체
    Page<Goods> findAllByRecommend(Pageable pageable, Integer totalElements);

    //
    Page<Goods> findGoodsByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);


    @Query(value = "select new map( count(g.id) as numberOfGoods, g.fishSpecies as fishSpecies )  FROM Goods g group by g.fishSpecies")
    List<Object> countTotalGoodsByFishSpecies();


    // @Query("select s.seaDirection, count(g.id) as cnt from Goods as g inner join Ship as s on s.id  = g.ship.id   group by s.seaDirection")
    @Query(value = "select new map( count(g.id) as numberOfGoods, g.ship.seaDirection as seaDirection )  FROM Goods g group by g.ship.seaDirection")
    List<Object> countTotalGoodsByRegion();

}
