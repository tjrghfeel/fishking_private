package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.model.admin.ShipManageDtoForPage;
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
                    "   s.profile_image thumbnailUrl, " +
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

    @Query(
            value = "select " +
                    "   s.id shipId, " +
                    "   s.ship_name name, " +
                    "   s.fishing_type fishingType, " +
                    "   s.address address, " +
                    "   s.distance distance, " +
                    "   s.profile_image thumbnailUrl, " +
                    "   if(f.file_type=3, true, false) isVideo " +
                    "from ship s left join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true and f.is_delete = false) " +
                    "       left join company c on s.company_id = c.id " +
                    "       left join member m on c.member_id = m.id " +
                    "where " +
                    "   m.session_token = :token " +
                    "   and  (s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% ) " +
                    "order by s.ship_name ",
            countQuery = "select s.id " +
                    "from ship s left join files f on (f.file_publish=0 and f.pid=s.id and f.is_represent=true and f.is_delete = false) " +
                    "       left join company c on s.company_id = c.id " +
                    "       left join member m on c.member_id = m.id " +
                    "where " +
                    "   m.session_token = :token " +
                    "   and  (s.ship_name like %:keyword% " +
                    "   or s.address like %:keyword% " +
                    "   or s.tel like %:keyword% " +
                    "   or s.sido like %:keyword% " +
                    "   or s.sigungu like %:keyword% ) " +
                    "order by s.ship_name ",
            nativeQuery = true
    )
    Page<ShipListForWriteFishingDiary> findBySearchKeywordCompany(@Param("keyword") String keyword, String token, Pageable pageable);

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

    //관리자항목. 선박 리스트 검색
    @Query(
            value = "select " +
                    "   s.id shipId, " +
                    "   s.ship_name shipName, " +
                    "   s.fishing_type fishingType, " +
                    "   s.address address, " +
                    "   s.tel tel, " +
                    "   (select group_concat(cc.code_name separator ',') from ship_fish_species sfs2, common_code cc " +
                    "       where sfs2.ship_id = s.id and sfs2.fish_species_id = cc.id group by sfs2.ship_id ) fishSpecies, " +
                    "   (select group_concat(cc.code_name separator ',') from ship_facilities sf2, common_code cc " +
                    "       where sf2.ship_id = s.id and sf2.facilities_id = cc.id group by sf2.ship_id ) facilities, " +
                    "   (select group_concat(cc.code_name separator ',') from ship_services ss2, common_code cc " +
                    "       where ss2.ship_id = s.id and ss2.services_id = cc.id group by ss2.ship_id ) services, " +
                    "   (select group_concat(cc.code_name separator ',') from ship_devices sd2, common_code cc " +
                    "       where sd2.ship_id = s.id and sd2.devices_id = cc.id group by sd2.ship_id ) devices, " +
                    "   if(rv.id is null, false, true) isLive, " +
                    "   c.company_name companyName, " +
                    "   s.total_avg_by_review totalAvgByReview, " +
                    "   s.is_active isActive, " +
                    "   s.depart_status departStatus, " +
                    "   c.member_id memberId " +
                    "from ship s left join ship_fish_species sfs on sfs.ship_id = s.id left join ship_facilities sf on sf.ship_id = s.id " +
                    "   left join ship_services ss on ss.ship_id = s.id left join ship_devices sd on sd.ship_id = s.id " +
                    "   left join realtime_video rv on rv.rtvideos_ship_id = s.id " +
                    "   join company c on c.id = s.company_id " +
                    "where " +
                    "   if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:fishingType is null, true, s.fishing_type = :fishingType) " +
                    "   and if(:address is null, true, s.address like %:address%) " +
                    "   and if(:tel is null, true, s.tel like %:tel%) " +
                    "   and if(:seaDirection is null, true, s.sea_direction = :seaDirection) " +
                    "   and if(:searchFishSpecies, (sfs.fish_species_id in :fishSpecies), true) " +
                    "   and if(:searchServices, (ss.services_id in :services), true) " +
                    "   and if(:searchFacilities, (sf.facilities_id in :facilities), true ) " +
                    "   and if(:searchDevices, (sd.devices_id in :devices), true) " +
                    "   and if(:isLive, rv.id is not null, true) " +
                    "   and if(:companyName is null, true, c.company_name like %:companyName%) " +
                    "   and if(:totalAvgByReview is null, true, s.total_avg_by_review > :totalAvgByReview) " +
                    "   and if(:isActive is null, true, s.is_active = :isActive) " +
                    "   and if(:departStatus is null, true, s.depart_status = :departStatus)  " +
                    "group by s.id ",
            countQuery = "select " +
                    "   s.id " +
                    "from ship s left join ship_fish_species sfs on sfs.ship_id = s.id left join ship_facilities sf on sf.ship_id = s.id " +
                    "   left join ship_services ss on ss.ship_id = s.id left join ship_devices sd on sd.ship_id = s.id " +
                    "   left join realtime_video rv on rv.rtvideos_ship_id = s.id " +
                    "   join company c on c.id = s.company_id " +
                    "where " +
                    "   if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:fishingType is null, true, s.fishing_type = :fishingType) " +
                    "   and if(:address is null, true, s.address like %:address%) " +
                    "   and if(:tel is null, true, s.tel like %:tel%) " +
                    "   and if(:seaDirection is null, true, s.sea_direction = :seaDirection) " +
                    "   and if(:searchFishSpecies, (sfs.fish_species_id in :fishSpecies), true) " +
                    "   and if(:searchServices, (ss.services_id in :services), true) " +
                    "   and if(:searchFacilities, (sf.facilities_id in :facilities), true ) " +
                    "   and if(:searchDevices, (sd.devices_id in :devices), true) " +
                    "   and if(:isLive, rv.id is not null, true) " +
                    "   and if(:companyName is null, true, c.company_name like %:companyName%) " +
                    "   and if(:totalAvgByReview is null, true, s.total_avg_by_review > :totalAvgByReview) " +
                    "   and if(:isActive is null, true, s.is_active = :isActive) " +
                    "   and if(:departStatus is null, true, s.depart_Status = :departStatus)  " +
                    "group by s.id ",
            nativeQuery = true
    )
    Page<ShipManageDtoForPage> getShipList(
        @Param("shipName") String shipName,
        @Param("fishingType") Integer fishingType,
        @Param("address") String address,
        @Param("tel") String tel,
        @Param("seaDirection") Integer seaDirection,
        @Param("searchFishSpecies") Boolean searchFishSpecies,
        @Param("fishSpecies") Long[] fishSpecies,
        @Param("searchServices") Boolean searchServices,
        @Param("services") Long[] services,
        @Param("searchFacilities") Boolean searchFacilities,
        @Param("facilities") Long[] facilities,
        @Param("searchDevices") Boolean searchDevices,
        @Param("devices") Long[] devices,
        @Param("isLive") Boolean isLive,
        @Param("companyName") String companyName,
        @Param("totalAvgByReview") Double totalAvgByReview,
        @Param("isActive") Boolean isActive,
        @Param("departStatus") Boolean departStatus,
        Pageable pageable
    );
}
