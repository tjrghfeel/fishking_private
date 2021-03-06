package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.model.admin.GoodsManageDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface GoodsRepository extends BaseRepository<Goods, Long>, GoodsRepositoryCustom {

    //추천업체
    Page<Goods> findAllByIsRecommend(Pageable pageable, Integer totalElements);

    //
    Page<Goods> findGoodsByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);


    @Query(value = "select new map( count(g.id) as numberOfGoods, g.fishSpecies as fishSpecies )  FROM Goods g group by g.fishSpecies")
    List<Object> countTotalGoodsByFishSpecies();

    // @Query("select s.seaDirection, count(g.id) as cnt from Goods as g inner join Ship as s on s.id  = g.ship.id   group by s.seaDirection")
    @Query(value = "select new map( count(g.id) as numberOfGoods, g.ship.seaDirection as seaDirection )  FROM Goods g group by g.ship.seaDirection")
    List<Object> countTotalGoodsByRegion();

    /* Goods에 대해 Member가 몇개나 찜했는지 쿼리. */
    @Query("select count(g) " +
            "from Goods as g " +
            "where g.id in (select t.linkId from Take as t where t.createdBy = :member and t.takeType = 0)")
    int findTakeCount(@Param("member") Member member);

    /*주어진 FishingType에 해당하는 Goods를 주어진 Member가 몇개나 찜했는지 쿼리. */
    @Query("select count(s) " +
            "from Ship as s " +
            "where s.fishingType = :fishingType and s.id in (select t.linkId from Take as t where t.createdBy = :member and t.takeType = 1)")
    int findTakeCountAboutFishType(@Param("member") Member member, @Param("fishingType") FishingType fishingType);

    /*인자 Member가 찜한 인자 FishingType에 해당하는 Goods 목록을 쿼리. */
    @Query(
            value = "select " +
                    "(select t2.id from take t2 where t2.take_type=1 and t2.link_id=s.id and t2.created_by=:member) takeId, " +
                    "s.id shipId, " +
                    "s.ship_name name, " +
//                    "(select group_concat(c.code_name separator ',') from goods_fish_species gs, common_code c " +
//                    "   where gs.goods_id = g.id and gs.fish_species_id = c.id group by gs.goods_id ) fishSpicesInfo, " +
//                    "(select cc.extra_value1 from common_code cc where cc.id=94) fishSpicesImgUrl, " +
//                    "(select count(c.id) from goods_fish_species gs, common_code c " +
//                        "where gs.goods_id = g.id and gs.fish_species_id = c.id ) fishSpicesCount, " +
                    "s.fishing_type fishingType, " +
                    "s.address address, " +
                    "s.distance distance, " +
//                    "g.total_amount price, " +
//                    "g.fishing_date fishingDate, " +
//                    "g.is_close isClose, " +
//                    "g.is_use isUse, " +
//                    "g.ship_start_time shipStartTime, " +
                    "(select min(total_amount) from goods where goods_ship_id = s.id) price, " +
                    "concat('/resource', s.profile_image) downloadThumbnailUrl, " +
//                    "f.thumbnail_file thumbnailFile, " +
//                    "f.file_url filePath, " +
                    "(select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) isLive " +
                    "from ship as s left join files as f on (s.id = f.pid and f.file_publish=0 and f.is_represent=1 and f.is_delete = false)  " +
                    "where s.fishing_type = :fishingType " +
                    "   and s.id in (select t.link_id from take as t where t.created_by = :member and t.take_type = 1) " +
                    "order by s.ship_name desc ",
            countQuery ="select s.id " +
                    "from ship as s left join files as f on (s.id = f.pid and f.file_publish=0 and f.is_represent=1 and f.is_delete = false)  " +
                    "where s.fishing_type = :fishingType " +
                    "   and s.id in (select t.link_id from take as t where t.created_by = :member and t.take_type = 1) " +
                    "order by s.ship_name desc ",
            nativeQuery = true
    )
    Page<TakeResponse> findTakeListAboutFishType(@Param("member") Member member, @Param("fishingType") int fishingType,Pageable pageable);

    //관리자페이지 - 상품목록 검색
    @Query(
            value = "select " +
                    "   g.id goodsId, " +
                    "   g.name goodsName, " +
                    "   g.total_amount price, " +
                    "   s.ship_name shipName, " +
                    "   (select group_concat(c.code_name separator ',') from goods_fish_species gs, common_code c " +
                    "       where gs.goods_id = g.id and gs.fish_species_id = c.id group by gs.goods_id ) fishSpecies, " +
                    "   p.member_id memberId, " +
                    "   g.is_use isUse " +
                    "from goods g left join goods_fishing_date d on d.goods_id = g.id,ship s join company p on s.company_id = p.id, goods_fish_species gs, common_code cc " +
                    "where " +
                    "   g.goods_ship_id = s.id " +
                    "   and gs.goods_id = g.id " +
                    "   and gs.fish_species_id = cc.id " +
                    "   and if(:isSpeciesList, (cc.code in :speciesList), true )  " +
//                    "   and if(:speciesList is null, true, cc.code in :speciesList) " +
                    "   and if(:goodsId is null, true, g.id = :goodsId) " +
                    "   and if(:goodsName is null, true, g.name like %:goodsName%) " +
                    "   and if(:priceStart is null, true, g.total_amount >= :priceStart) " +
                    "   and if(:priceEnd is null, true, g.total_amount <= :priceEnd) " +
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:fishingDateStart is null, true, DATE(d.fishing_date) >= :fishingDateStart) " +
                    "   and if(:fishingDateEnd is null, true, DATE(d.fishing_date) <= :fishingDateEnd) " +
                    "group by g.id " +
                    "order by g.created_date desc ",
            countQuery = "select " +
                    "   g.id " +
                    "from goods g left join goods_fishing_date d on d.goods_id = g.id,ship s, goods_fish_species gs, common_code cc " +
                    "where " +
                    "   g.goods_ship_id = s.id " +
                    "   and gs.goods_id = g.id " +
                    "   and gs.fish_species_id = cc.id " +
                    "   and if(:isSpeciesList, (cc.code in :speciesList), true )  " +
//                    "   and if(:speciesList is null, true, cc.code in :speciesList) " +
                    "   and if(:goodsId is null, true, g.id = :goodsId) " +
                    "   and if(:goodsName is null, true, g.name like %:goodsName%) " +
                    "   and if(:priceStart is null, true, g.total_amount >= :priceStart) " +
                    "   and if(:priceEnd is null, true, g.total_amount <= :priceEnd) " +
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:fishingDateStart is null, true, DATE(d.fishing_date) >= :fishingDateStart) " +
                    "   and if(:fishingDateEnd is null, true, DATE(d.fishing_date) <= :fishingDateEnd) " +
                    "group by g.id " +
                    "order by g.created_date desc ",
            nativeQuery = true
    )
    Page<GoodsManageDtoForPage> getGoodsList(
        @Param("goodsId") Long goodsId,
        @Param("goodsName") String goodsName,
        @Param("priceStart") Integer priceStart,
        @Param("priceEnd") Integer priceEnd,
        @Param("shipName") String shipName,
        @Param("fishingDateStart") LocalDate fishingDateStart,
        @Param("fishingDateEnd") LocalDate fishingDateEnd,
        @Param("isSpeciesList") Boolean isSpeciesList,
        @Param("speciesList") String[] speciesList,
        Pageable pageable
    );

    //관리자 - 대시보드 - 상품현황
    @Query("select " +
            "(select count(g.id) from Goods g where g.isUse = true and g.isClose = false and g.ship.fishingType=0) as ship, " +
            "(select count(g.id) from Goods g where g.isUse =true and g.isClose = false and g.ship.fishingType=1) as searock " +
            "from Goods g2 ")
    List<Map<String, Long>> getDashBoardGoods(Pageable pageable);

    @Query("select min(g.totalAmount) from Goods g where g.isUse = true and g.ship.id = :shipId")
    Integer getCheapestGoods(Long shipId);
}
