package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.model.fishing.LiveShipDtoForPage;
import com.tobe.fishking.v2.model.fishing.ShipListForWriteFishingDiary;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;


public interface ShipRepository extends BaseRepository<Ship, Long>, ShipRepositoryCustom {


    /*member가 만든 company가 만든 ship을 가져오는 메소드.
     * - !!!!! company와 ship은 '1대다'관계로 db가 구성되어있지만, 부장님말씀대로 1대1이라 가정하고 만든 쿼리. */
    @Query("select s from Ship s where s.company = (select c from Company c where c.member = :member) ")
    Ship findByMember(@Param("member") Member member);

    /*글쓰기 - 업체검색 */
    @Query(
            value = "select " +
                    "   s.id shipId, " +
                    "   s.ship_name name, " +
                    "   s.fishing_type fishingType, " +
                    "   s.address address, " +
                    "   s.distance distance, " +
                    "   f.file_url fileUrl, " +
                    "   f.thumbnail_file fileName," +
                    "   if(f.file_type=3, true, false) isVideo " +
                    "from ship s left join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true and f.is_delete = false) " +
                    "where " +
                    "   s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% " +
                    "order by s.ship_name ",
            countQuery = "select s.id " +
                    "from ship s join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true and f.is_delete = false) " +
                    "where " +
                    "   s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% " +
                    "order by s.ship_name  ",
            nativeQuery = true
    )
    Page<ShipListForWriteFishingDiary> findBySearchKeyword(@Param("keyword") String keyword, Pageable pageable);

    /*상품이 속한 ship을 검색하는 메소드*/
    @Query("select s from Ship s  where s = (select g.ship from Goods g where g = :goods)")
    Ship findByGoods(@Param("goods") Goods goods);

/*
    @Query("select s from Ship s \n" +
            " left outer join fetch s.fishSpecies \n" +
            " join s.location pi where pi.longitude is not null or pi.longitude is not null ")
    List<Ship> findAllShipAndLocation();

*/
    /*@Query("select s from Ship s \n" +
            " left outer join fetch s.fishSpecies r \n" +
            " join s.location pi  \n" +
            " join s.fishingType ft  \n" +
            " where pi.longitude is not null and pi.longitude is not null  and ft = :fishingType ")
    */


    @Query(value="select s.* from Ship s \n" +
            " where s.longitude is not null and s.longitude is not null  and s.fishing_type = :fishingType ", nativeQuery = true)
    List<Ship> findAllShipAndLocationByFishingType(@Param("fishingType") FishingType fishingType);

    // List<Ship> findAll(Specification<Ship> ShipSpecification);


    @Query(value = "SELECT COUNT(id) FROM Ship WHERE isActive = true")
    Long findAllByIsActive();

    // _shipRepository.count(new GroupBySpecification(toSpecification(filter),parameters));

    /*실시간 조항 ship 검색.*/
    @Query(
            value = "select " +
                    "s.id shipId, " +
                    "s.ship_name name, " +
                    "s.fishing_type fishingType, " +
                    "s.address address, " +
                    "s.distance distance, " +
                    "(select min(total_amount) from goods where goods_ship_id = s.id) price, " +
                    "f.thumbnail_file thumbnailFile, " +
                    "f.file_url filePath, " +
                    "(select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) isLive " +
                    "from ship as s left join files as f on (s.id = f.pid and f.file_publish=0 and f.is_represent=1 and f.is_delete = false)  " +
                    "where " +
                    "   exists (select v.id from realtime_video v where v.rtvideos_ship_id = s.id) " +
                    "order by s.ship_name desc ",
            countQuery ="select s.id " +
                    "from ship as s left join files as f on (s.id = f.pid and f.file_publish=0 and f.is_represent=1 and f.is_delete = false)  " +
                    "where " +
                    "   exists (select v.id from realtime_video v where v.rtvideos_ship_id = s.id) " +
                    "order by s.ship_name desc ",
            nativeQuery = true
    )
    Page<LiveShipDtoForPage> getLiveShipList(Pageable pageable);

    @Query("select g.ship.shipName from Goods g where g.id = :goodsId")
    String getShipNameByGoodsId(Long goodsId);
}
