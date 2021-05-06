package com.tobe.fishking.v2.controller.post;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.QOrders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.police.PoliceGoodsResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;
import com.tobe.fishking.v2.model.smartsail.TodayBoardingResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import com.tobe.fishking.v2.service.smartsail.BoardingService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslTest {

    @Autowired
    private CalculateRepository calculateRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CommonCodeRepository commonCodeRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private RideShipRepository rideShipRepository;
    @Autowired
    private BoardingService boardingService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void querydslCustomTest() {
        //given
        Long memberId = 13L;

        //when
//        Map<String, Object> r = goodsService.getRideData(1L);
//        Page<PoliceGoodsResponse> responses = goodsRepository.getPoliceGoods(0);
//        Page<PoliceGoodsResponse> responses = goodsRepository.getPoliceGoods(0);
//        List<CalculateResponse> result = calculateRepository.searchCalculate(memberId, "", "2021", "02", null);
//        for (Tuple r : result) {
//            System.out.println(r.get(0, Integer.class) + ", " + r.get(1, Integer.class) + ", " + r.get(2, Long.class) + ", " + r.get(3, Long.class) + ", " + r.get(3, Long.class));
////            System.out.println(r.get(0, Integer.class) + ", " + r.get(1, Integer.class));
//        }
//        List<CommonCode> responses = commonCodeRepository.getShipSpeciesName(20L);
//        responses.forEach(commonCode -> {
//            System.out.println(commonCode.getCodeName());
//        });
//        Page<ShipListResponse> responses = shipRepository.searchAll()

//        List<Map<String, String>> responses = calculateRepository.calculateDetailForExcel(19L, "2021", "02");
//        for(Map<String, String> r : responses) {
//            System.out.println(r);
//        }
//        List<TodayBoardingResponse> l = rideShipRepository.getTodayRiders(13L, "shipName");
//        Member member = memberRepository.getOne(memberId);
//        List<TodayBoardingResponse> l = boardingService.getTodayBoarding(member, "shipName");

//        List<Goods> goods = goodsRepository.getNeedConfirm("2021-05-06", "14");
        List<Integer> goods = new ArrayList<>();
        int idx = 2;
        while (idx > 0) {
            goods.add(idx);
            idx -= 1;
        }
        System.out.println(goods.size() + " " + goods.size()/2);
        goods = goods.subList(goods.size()/2, goods.size());
        System.out.println(goods.size());

        //then
        assertThat(goods.size(), is(2));
//        System.out.println(r);
//        assertThat(r.size(), is(0L));
    }

}
