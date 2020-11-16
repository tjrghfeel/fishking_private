package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertsRepository extends BaseRepository<Alerts, Long> {
    Alerts findByCreatedBy(Member member);
}
