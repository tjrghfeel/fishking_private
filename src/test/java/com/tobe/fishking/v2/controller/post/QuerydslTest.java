package com.tobe.fishking.v2.controller.post;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.fishing.QOrders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.fishking.CalculateRepository;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslTest {

    @Autowired
    private CalculateRepository calculateRepository;

    @Test
    public void querydslCustomTest() {
        //given
        Long memberId = 13L;

        //when
        List<CalculateResponse> result = calculateRepository.searchCalculate(memberId, "", "2021", "02", null);
//        for (Tuple r : result) {
//            System.out.println(r.get(0, Integer.class) + ", " + r.get(1, Integer.class) + ", " + r.get(2, Long.class) + ", " + r.get(3, Long.class) + ", " + r.get(3, Long.class));
////            System.out.println(r.get(0, Integer.class) + ", " + r.get(1, Integer.class));
//        }
        //then
        assertThat(result.size(), is(1));
    }

}
