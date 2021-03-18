package com.tobe.fishking.v2.controller.post;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.fishing.QOrders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.repository.board.PostRepository;
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
    private OrdersRepository ordersRepository;

    @Test
    public void querydslCustomTest() {
        //given
        Long memberId = 13L;

        //when
        List<Tuple> result = ordersRepository.getStatus(memberId);
        for (Tuple r : result) {
            System.out.println(r.get(0, OrderStatus.class) + ": " + r.get(1, Long.class));
        }
        //then
        assertThat(result.size(), is(5));
    }

}
