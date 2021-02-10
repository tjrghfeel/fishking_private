package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.entity.fishing.TidalLevel;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TidalLevelRepository extends JpaRepository<TidalLevel, Long> {

    List<TidalLevel> findAllByDate(LocalDate date);

    @Query("select t from TidalLevel t where t.date = :date and (t.peak='high' or t.peak='low') and t.observerCode=:observer order by t.dateTime asc")
    List<TidalLevel> findAllByDateAndIsHighWaterAndIsLowWaters(@Param("date") LocalDate date, @Param("observer")ObserverCode observer);

    /*@Query("select t from TidalLevel t " +
            "where t.dateTime > :now and t.peak=:peak and t.observerCode = :observer order by t.dateTime asc limit 2")
    List<TidalLevel> getPeak(@Param("now")LocalDateTime now, @Param("peak") String peak, @Param("observer") ObserverCode observer);*/
    List<TidalLevel> findTop2ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
            LocalDateTime dateTime, String peak, ObserverCode observerCode
    );
}
