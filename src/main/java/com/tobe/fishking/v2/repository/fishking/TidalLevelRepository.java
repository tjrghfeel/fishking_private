package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.fishing.PhoneAuth;
import com.tobe.fishking.v2.entity.fishing.TidalLevel;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TidalLevelRepository extends JpaRepository<TidalLevel, Long> {

    List<TidalLevel> findAllByDate(LocalDate date);

}
