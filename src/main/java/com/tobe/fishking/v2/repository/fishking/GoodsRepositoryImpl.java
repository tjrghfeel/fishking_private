package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id) {
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods
                ))
                .from(goods)
                .where(goods.ship.id.eq(ship_id))
                .fetchResults();
        return result.getResults();
    }
}
