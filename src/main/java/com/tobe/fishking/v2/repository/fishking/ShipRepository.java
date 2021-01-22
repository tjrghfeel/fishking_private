package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.fishing.ShipListForWriteFishingDiary;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShipRepository extends BaseRepository<Ship, Long> {

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
                    "from ship s left join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true) " +
                    "where " +
                    "   s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% " +
                    "order by name ",
            countQuery = "select s.id " +
                    "from ship s join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true) " +
                    "where " +
                    "   s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% " +
                    "order by name ",
            nativeQuery = true
    )
    Page<ShipListForWriteFishingDiary> findBySearchKeyword(@Param("keyword") String keyword, Pageable pageable);

    /*상품이 속한 ship을 검색하는 메소드*/
    @Query("select s from Ship s  where s = (select g.ship from Goods g where g = :goods)")
    Ship findByGoods(@Param("goods") Goods goods);
}
