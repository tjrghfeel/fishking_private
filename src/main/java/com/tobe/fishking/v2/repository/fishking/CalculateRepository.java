package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Calculate;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculateRepository extends BaseRepository<Calculate, Long>, CalculateRepositoryCustom {
}
