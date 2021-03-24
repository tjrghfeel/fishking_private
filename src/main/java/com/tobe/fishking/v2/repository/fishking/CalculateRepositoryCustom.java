package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;

import java.util.List;

public interface CalculateRepositoryCustom {
    List<CalculateResponse> searchCalculate(Long memberId, String shipName, String year, String month, Boolean status);
}
