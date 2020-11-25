package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.enums.common.TakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoveToRepository extends JpaRepository<LoveTo, Long> {

    int countByTakeTypeAndLinkId(TakeType takeType, Long linkId);

    List<LoveTo> findByCreatedBy(Member member);

}
