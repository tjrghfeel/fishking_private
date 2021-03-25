package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.model.smartfishing.CalculateDetailResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;

import java.util.List;

public interface CalculateRepositoryCustom {
    List<CalculateResponse> searchCalculate(Long memberId, String shipName, String year, String month, Boolean status);
    List<CalculateDetailResponse> calculateDetail(Long shipId, String year, String month);
}
