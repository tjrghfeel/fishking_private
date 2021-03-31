package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.model.common.AlertListForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertsRepository extends BaseRepository<Alerts, Long> {
    Alerts findByCreatedBy(Member member);

    /*@Query("select new com.tobe.fishking.v2.model.NoNameDTO(a.id, a.alert_sets) from Alerts a")
    List<NoNameDTO> find();*/

    @Query(
            value = "select " +
                    "   a.id alertId, " +
                    "   a.alert_type alertType, " +
                    "   a.alert_time createdDate, " +
                    "   a.sentence content, " +
                    "   cc.extra_value1 iconDownloadUrl " +
                    "from alerts a, common_code cc " +
                    "where " +
                    "   (a.is_sent = true or a.alert_time < :today) " +
                    "   and cc.code_group_id = 93 " +
                    "   and cc.code = a.alert_type " +
                    "   and a.receiver_id = :memberId " +
                    "   and a.is_read = false " +
                    "order by a.alert_time desc",
            countQuery = "select a.id " +
                    "from alerts a, common_code cc " +
                    "where " +
                    "   (a.is_sent = true or a.alert_time < :today) " +
                    "   and cc.code_group_id = 93 " +
                    "   and cc.code = a.alert_type " +
                    "   and a.receiver_id = :memberId " +
                    "   and a.is_read = false " +
                    "order by a.alert_time desc",
            nativeQuery = true
    )
    Page<AlertListForPage> findAllByMember(@Param("memberId") Long memberId, @Param("today") LocalDateTime today, Pageable pageable);

    /*세션토큰에 해당하는 회원의 알림 개수 카운트*/
    @Query(
            value = "select count(a.id) " +
                    "from alerts a " +
                    "where " +
                    "   (a.is_sent = true or a.alert_time < NOW()) " +
                    "   and a.receiver_id = :memberId " +
                    "   and a.is_read = false ",
            nativeQuery = true
    )
    int countByMember(@Param("memberId") Long memberId);

    List<Alerts> findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
            LocalDateTime alertTime, Member receiver, AlertType alertType, Long pid, Boolean isSent);

    Boolean existsByAlertTimeGreaterThanAndReceiverAndPidAndEntityTypeAndAlertTypeAndIsSent(
            LocalDateTime alertTime, Member receiver, Long pid, EntityType entityType, AlertType alertType, Boolean isSent);

    List<Alerts> findAllByAlertTypeAndIsSentAndAlertTime(AlertType alertType, Boolean isSent, LocalDateTime alertTime);
}
