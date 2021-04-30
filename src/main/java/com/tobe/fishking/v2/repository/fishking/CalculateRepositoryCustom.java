package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.model.smartfishing.CalculateDetailResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;

import java.util.List;
import java.util.Map;

public interface CalculateRepositoryCustom {
    List<CalculateResponse> searchCalculate(Long memberId, String shipName, String year, String month, Boolean status);
    List<CalculateDetailResponse> calculateDetail(Long shipId, String year, String month);
    List<Map<String, String>> calculateDetailForExcel(Long shipId, String year, String month);
}
