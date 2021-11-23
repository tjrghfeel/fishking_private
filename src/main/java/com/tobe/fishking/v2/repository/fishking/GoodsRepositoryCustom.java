package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.model.police.PoliceGoodsResponse;
import com.tobe.fishking.v2.model.police.RiderResponse;
import com.tobe.fishking.v2.model.response.GoodsSmallResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface GoodsRepositoryCustom {
    List<GoodsResponse> getShipGoods(Long ship_id, LocalDate date);
    List<GoodsResponse> getShipGoods(Long ship_id);
    List<GoodsSmallResponse> searchGoods(Long shipId, String keyword, String status);
    UpdateGoodsResponse getGoodsData(Long goodsId);
    Long getTodayRunGoods();
    Long getNowRunGoods();
    Long getWaitRidePersonnel();
    Long getRealRidePersonnel();
    List<PoliceGoodsResponse> getPoliceAllGoods();
    Page<PoliceGoodsResponse> getPoliceGoods(Integer page);
    List<RiderResponse> getRiderData(Long goodsId);
    List<Goods> getNeedConfirm(String date, String time);
    Page<Goods> getDayFishing(String date, Member member, Integer page);
}
