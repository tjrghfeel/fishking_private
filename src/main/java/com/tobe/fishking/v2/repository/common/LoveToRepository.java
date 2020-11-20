package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.enums.common.TakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoveToRepository extends JpaRepository<LoveTo, Long> {

    int countByTaskTypeAndLinkId(TakeType takeType, Long linkId);


}
