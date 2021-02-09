package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.TidalLevel;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TidalLevelRepository extends BaseRepository<TidalLevel, Long>, TidalLevelRepositoryCustom {
//    @Query("select l.observerCode.code, l.level, l.date, l.dateTime, l.peak from TidalLevel l where l.date = :date and l.observerCode.code = :code")
//    List<TidalLevelResponse> findAllByDateAndCode(LocalDate date, String code);
}
