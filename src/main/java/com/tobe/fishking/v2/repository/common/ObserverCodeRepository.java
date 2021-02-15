package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.model.fishing.ObserverDtoList;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObserverCodeRepository extends BaseRepository<ObserverCode, Long> {

    @Query(
            value = "select " +
                    "   o.id observerId, " +
                    "   o.name observerName, " +
                    "   o.code observerCode, " +
                    "   if( (select count(a.id) from alerts a " +
                    "           where a.receiver_id = :memberId and o.id = a.pid and a.entity_type = 20 " +
                    "           and a.alert_type = :alertType), true, false) isAlerted " +
                    "from observer_code o " +
                    "",
            countQuery = "select o.id from observer_code o ",
            nativeQuery = true
    )
    List<ObserverDtoList> getObserverList(@Param("memberId") Long memberId, @Param("alertType") AlertType alertType);

}
