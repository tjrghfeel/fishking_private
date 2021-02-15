package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.common.TidalLevel;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TidalLevelRepository extends BaseRepository<TidalLevel, Long>, TidalLevelRepositoryCustom {
    List<TidalLevel> findAllByDate(LocalDate date);
//    @Query("select l.observerCode.code, l.level, l.date, l.dateTime, l.peak from TidalLevel l where l.date = :date and l.observerCode.code = :code")
//    List<TidalLevelResponse> findAllByDateAndCode(LocalDate date, String code);
    @Query("select t from TidalLevel t where t.date = :date and (t.peak='high' or t.peak='low') and t.observerCode=:observer order by t.dateTime asc")
    List<TidalLevel> findAllByDateAndIsHighWaterAndIsLowWaters(@Param("date") LocalDate date, @Param("observer") ObserverCode observer);

    /*@Query("select t from TidalLevel t " +
            "where t.dateTime > :now and t.peak=:peak and t.observerCode = :observer order by t.dateTime asc limit 2")
    List<TidalLevel> getPeak(@Param("now")LocalDateTime now, @Param("peak") String peak, @Param("observer") ObserverCode observer);*/
    List<TidalLevel> findTop2ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
            LocalDateTime dateTime, String peak, ObserverCode observerCode
    );
}
