package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.common.Event;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.common.EventDtoForPage;
import com.tobe.fishking.v2.model.fishing.ShipEventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e.title from Event e where e.ship.id = :ship_id")
    List<String> getEventTitleByShip(Long ship_id);

    @Query("select e from Event e where e.ship.id = :ship_id")
    List<ShipEventResponse> getEventByShip(Long ship_id);

    @Query("select e from Event e where e.ship.id = :ship_id and e.isDeleted = false")
    List<Event> getEventByShipActive(Long ship_id);

    /*이벤트 목록 가져오기*/
    @Query(value = "" +
            "select " +
            "   e.id eventId, " +
            "   s.id shipId, " +
            "   if((select count(f.id) from files f where f.pid=e.id and f.file_publish=13 and f.is_delete=false)>0," +
            "       (select concat('/',f.file_url,'/',f.stored_file) from files f " +
            "           where f.pid=e.id and f.file_publish=13 and f.is_delete=false limit 1), " +
            "       (select c.extra_value1 from common_code c where c.code_group_id=154 and c.code='eventDefault')" +
            "   ) imageUrl, " +
            "   e.title eventTitle, " +
            "   LEFT(e.contents,30) content, " +
            "   s.ship_name shipName, " +
            "   e.start_day startDay, " +
            "   e.end_day endDay, " +
            "   e.is_active isActive " +
            "from event e left join ship s on e.ship_id = s.id, member m " +
            "where if(:isLast = false or :isLast is null, e.end_day >= :today, e.end_day < :today) " +
            "   and e.created_by = m.id " +
            "   and e.is_deleted = false " +
            "   and if(:title is null, true, e.title like %:title%) " +
            "   and if(:content is null, true, e.contents like %:content%) " +
            "   and if(:createdDateStart is null, true, e.created_date >= :createdDateStart) " +
            "   and if(:createdDateEnd is null, true, e.created_date <= :createdDateEnd) " +
            "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
            "   and if(:startDate is null, true, :startDate <= e.start_day) " +
            "   and if(:endDate is null, true, :endDate >= e.end_day) " +
            "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
            "   and if(:isActive is null, true, e.is_active = :isActive) " +
            "   and if(:shipEvent is null, true, if(:shipEvent = true, e.ship_id is not null, e.ship_id is null))" +
            "order by e.order_level desc, e.end_day asc, e.start_day asc, e.like_count desc, e.created_date asc " +
            "",
            countQuery = "select e.id " +
                    "from event e left join ship s on e.ship_id = s.id, member m " +
                    "where if(:isLast = false or :isLast is null, e.end_day >= :today, e.end_day < :today) " +
                    "   and e.created_by = m.id " +
                    "   and e.is_deleted = false " +
                    "   and if(:title is null, true, e.title like %:title%) " +
                    "   and if(:content is null, true, e.contents like %:content%) " +
                    "   and if(:createdDateStart is null, true, e.created_date >= :createdDateStart) " +
                    "   and if(:createdDateEnd is null, true, e.created_date <= :createdDateEnd) " +
                    "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
                    "   and if(:startDate is null, true, :startDate <= e.start_day) " +
                    "   and if(:endDate is null, true, :endDate >= e.end_day) " +
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:isActive is null, true, e.is_active = :isActive) " +
                    "   and if(:shipEvent is null, true, if(:shipEvent = true, e.ship_id is not null, e.ship_id is null))" +
                    "order by e.order_level desc, e.end_day asc, e.start_day asc, e.like_count desc, e.created_date asc " +
                    "",
            nativeQuery = true
    )
    Page<EventDtoForPage> findEventList(
            @Param("today") String today,
            @Param("isLast") Boolean isLast,
            @Param("title") String title,
            @Param("content") String content,
            @Param("createdDateStart") String createdDateStart,
            @Param("createdDateEnd") String createdDateEnd,
            @Param("nickName") String nickName,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("shipName") String shipName,
            @Param("isActive") Boolean isActive,
            @Param("shipEvent") Boolean shipEvent,
            Pageable pageable
    );
}