package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.tobe.fishking.v2.entity.common.QTidalLevel.tidalLevel;

@RequiredArgsConstructor
public class TidalLevelRepositoryImpl implements TidalLevelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TidalLevelResponse> findAllByDateAndCode(LocalDate date, String code) {
        QueryResults<TidalLevelResponse> results = queryFactory
                .select(Projections.constructor(TidalLevelResponse.class,
                        tidalLevel.level,
                        tidalLevel.dateTime,
                        tidalLevel.peak
                ))
                .from(tidalLevel)
                .where(tidalLevel.date.eq(date), tidalLevel.observerCode.code.eq(code), tidalLevel.peak.isNotNull())
                .fetchResults();
        return results.getResults();
    }
}
