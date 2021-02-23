package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.tobe.fishking.v2.entity.common.QLoveTo.loveTo;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id, LocalDate date) {
        NumberPath<Integer> countAlias = Expressions.numberPath(Integer.class, "count");
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(orderDetails.personnel.sum())
                                        .from(orderDetails)
                                        .where(orderDetails.goods.id.eq(goods.id), orderDetails.orders.fishingDate.eq(DateUtils.getDateInFormat(date)))
                                , countAlias)
                ))
                .from(goods)
                .where(goods.ship.id.eq(ship_id), goods.isUse.eq(true), goods.fishingDates.any().fishingDate.eq(date))
                .fetchResults();
        return result.getResults();
    }

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id) {
        NumberPath<Integer> countAlias = Expressions.numberPath(Integer.class, "count");
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods
                ))
                .from(goods)
                .where(goods.ship.id.eq(ship_id), goods.isUse.eq(true))
                .fetchResults();
        return result.getResults();
    }
}
