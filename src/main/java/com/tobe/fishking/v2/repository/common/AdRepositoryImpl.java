package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.model.fishing.SmallShipResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.common.QAd.ad;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class AdRepositoryImpl implements AdRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SmallShipResponse> getAdByType(AdType type) {
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");

        QueryResults<SmallShipResponse> results = queryFactory
                .select(Projections.constructor(SmallShipResponse.class,
                        ship.cheapestGoodsCost,
                        ship.id,
                        ship.profileImage,
                        ship.shipName,
                        ship.sido,
                        ship.sigungu,
                        ship.location,
                        ship.address,
                        ship.fishingType
                ))
                .from(ad)
                .where(ad.adType.eq(type),
                        ad.ship.isActive.eq(true),
                        ad.ship.company.isOpen.eq(true)
                )
                .fetchResults();
        return results.getResults();
    }

}
