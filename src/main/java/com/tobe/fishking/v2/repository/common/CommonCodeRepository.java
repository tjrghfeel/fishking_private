package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {

    @Query("select a from CommonCode a where a.codeName = :codeName")
    public Optional<CommonCode> findByCodeName(String codeName);

    @Query("select a from CommonCode a where a.codeName = :codeName and a.codeGroup = :codeGroup")
    public Optional<CommonCode> findByCodeNameAndDiv(String codeName, CodeGroup codeGroup);
}
