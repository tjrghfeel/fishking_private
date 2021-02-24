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

    /*이벤트 목록 가져오기*/
    @Query(value = "" +
            "select " +
            "   e.id eventId, " +
            "   s.id shipId, " +
//            "   if(f.stored_file is null, " +
//            "       (select c.extra_value1 from common_code c where c.code_group_id=154 and c.code='eventDefault'), " +
//            "       f.stored_file) fileName, " +
//            "   if(f.file_url is null, 'common', f.file_url) filePath, " +
            "   if(f.stored_file is null, " +
            "       (select c.extra_value1 from common_code c where c.code_group_id=154 and c.code='eventDefault'), " +
            "       concat('/',f.file_url,'/',f.stored_file)" +
            "   ) imageUrl, " +
            "   e.title eventTitle, " +
            "   s.ship_name shipName, " +
            "   e.start_day startDay, " +
            "   e.end_day endDay " +
            "from event e join ship s on e.ship_id = s.id left join files f on f.pid=e.id and f.file_publish=13 " +
            "where e.end_day > :today " +
            "group by f.pid " +
            "order by e.end_day asc, e.start_day asc, e.like_count desc, e.created_date asc " +
            "",
            countQuery = "select e.id " +
                    "from event e join ship s on e.ship_id = s.id left join files f on f.pid=e.id and f.file_publish=13  " +
                    "where e.end_day > :today " +
                    "group by f.pid " +
                    "order by e.end_day asc, e.start_day asc, e.like_count desc, e.created_date asc " +
                    "",
            nativeQuery = true
    )
    Page<EventDtoForPage> findEventList(@Param("today") String today, Pageable pageable);
}