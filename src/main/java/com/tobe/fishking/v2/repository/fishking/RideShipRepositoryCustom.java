package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.fishing.RiderFingerPrint;
import com.tobe.fishking.v2.model.smartsail.RiderGoodsListResponse;
import com.tobe.fishking.v2.model.smartsail.RiderSearchDTO;
import com.tobe.fishking.v2.model.smartsail.TodayBoardingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RideShipRepositoryCustom {
    List<TodayBoardingResponse> getTodayRiders(Long memberId, String orderBy, Boolean comp);
    Map<String, Object> dashboard(Long memberId);
    RiderFingerPrint getFingerPrint(String name, String phone);
    Page<RiderGoodsListResponse> searchRiders(Long memberId, RiderSearchDTO dto, Pageable pageable);
    List<Tuple> getDetailRiders(Long orderId);
    Map<String, Object> dashboardForManage();
}
