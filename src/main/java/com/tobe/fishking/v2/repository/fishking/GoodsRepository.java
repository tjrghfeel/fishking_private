package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.TakeResponse;
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

    /*주어진 FishingType에 해당하는 Goods를 주어진 Member가 몇개나 찜했는지 쿼리. */
    @Query("select count(g) " +
            "from Goods as g " +
            "where g.fishingType = :fishingType and g.id in (select t.linkId from Take as t where t.createdBy = :member and t.takeType = 0)")
    int findTakeCountAboutFishType(Member member, FishingType fishingType);

    /*인자 Member가 찜한 인자 FishingType에 해당하는 Goods 목록을 쿼리. */
    @Query(
            value = "select " +
                    "g.id id, " +
                    "g.name name, " +
                    "g.fishing_date fishingDate, " +
                    "g.is_close isClose, " +
                    "g.is_use isUse, " +
                    "g.ship_start_time shipStartTime, " +
                    "g.total_amount totalAmount, " +
                    "g.total_avg_by_review totalAvgByReview, " +
                    "p.palce_name placeName, " +
                    "p.fish_spices_info fishSpicesInfo " +
                    "from goods as g, places as p " +
                    "where g.fishing_type = :fishingType " +
                    "   and g.id in (select t.link_id from take as t where t.created_by = :member and t.take_type = 0) " +
                    "   and g.goods_place_id = p.id ",
            countQuery ="select g.id " +
                        "from goods as g, places as p " +
                        "where g.fishing_type = :fishingType " +
                        "and g.id in (select t.link_id from take as t where t.created_by = :member and t.take_type = 0) " +
                        "and g.goods_place_id = p.id ",
            nativeQuery = true
    )
    List<TakeResponse> findTakeListAboutFishType(Member member, int fishingType);
}
