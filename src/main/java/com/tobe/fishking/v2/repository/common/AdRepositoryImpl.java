package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.common.QAd;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.SmallShipResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.common.QAd.ad;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class AdRepositoryImpl implements AdRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SmallShipResponse> getAdByType(AdType type) {
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");

        QueryResults<SmallShipResponse> results = queryFactory
                .select(Projections.constructor(SmallShipResponse.class,
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ad.ship.id)), aliasPrice),
                        ship
                ))
                .from(ad)
                .where(ad.adType.eq(type))
                .fetchResults();
        return results.getResults();
    }
}
