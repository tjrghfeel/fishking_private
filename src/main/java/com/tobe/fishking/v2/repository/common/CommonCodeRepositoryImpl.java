package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.model.common.MainSpeciesResponse;
import com.tobe.fishking.v2.model.fishing.SmallShipResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.common.QCommonCode.commonCode;
import static com.tobe.fishking.v2.entity.common.QObserverCode.observerCode;
import static com.tobe.fishking.v2.entity.common.QTidalLevel.tidalLevel;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class CommonCodeRepositoryImpl implements CommonCodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MainSpeciesResponse> getMainSpeciesCount() {
        NumberPath<Long> aliasCount = Expressions.numberPath(Long.class, "count");

        QueryResults<MainSpeciesResponse> results = queryFactory
                .select(Projections.constructor(MainSpeciesResponse.class,
                    commonCode.code,
                    commonCode.codeName,
                    commonCode.remark,
                    ExpressionUtils.as(JPAExpressions
                            .select(ship.count())
                            .from(ship)
                            .where(ship.fishSpecies.contains(commonCode)
                                    .and(ship.isActive.eq(true))
                            ), aliasCount)
                ))
                .from(commonCode)
                .where(commonCode.codeGroup.id.eq(80L), commonCode.isActive.eq(true))
                .orderBy(commonCode.orderBy.asc())
                .fetchResults();
        return results.getResults();
    }

    @Override
    public List<MainSpeciesResponse> getMainDistrictCount() {
        QueryResults<MainSpeciesResponse> results = queryFactory
                .select(Projections.constructor(MainSpeciesResponse.class,
                        commonCode.extraValue1,
                        commonCode.codeName,
                        commonCode.remark,
                        ship.count()
                ))
                .from(ship).join(observerCode).on(ship.observerCode.eq(observerCode.code)).join(commonCode).on(observerCode.forecastCode.eq(commonCode.code))
                .groupBy(observerCode.forecastCode)
                .fetchResults();
        return results.getResults();
    }
}
