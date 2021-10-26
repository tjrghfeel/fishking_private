package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Sailor;
import com.tobe.fishking.v2.model.smartsail.SailorResponse;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SailorRepository extends BaseRepository<Sailor, Long> {

    @Query("select s from Sailor s where s.createdBy = :member")
    List<SailorResponse> findAllByCreatedBy(Member member);

}
