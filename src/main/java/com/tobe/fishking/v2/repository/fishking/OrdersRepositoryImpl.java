package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.Orders;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Orders> getCheckOrders() {

        return null;
    }

}
