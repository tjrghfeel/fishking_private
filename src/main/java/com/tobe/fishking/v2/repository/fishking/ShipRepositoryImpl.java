package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class ShipRepositoryImpl implements ShipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Ship> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable) {
        QueryResults<Ship> results = queryFactory
                .selectFrom(ship)
                .where()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
