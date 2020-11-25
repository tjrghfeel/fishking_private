package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertsRepository extends BaseRepository<Alerts, Long> {
    Alerts findByCreatedBy(Member member);

    /*@Query("select new com.tobe.fishking.v2.model.NoNameDTO(a.id, a.alert_sets) from Alerts a")
    List<NoNameDTO> find();*/
}
