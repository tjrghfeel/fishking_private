package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.model.response.TidalLevelResponse;

import java.time.LocalDate;
import java.util.List;

public interface TidalLevelRepositoryCustom {
    List<TidalLevelResponse> findAllByDateAndCode(LocalDate date, String code);
}
