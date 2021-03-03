package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.common.SearchKeyword;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.common.QSearchKeyword.searchKeyword1;

@RequiredArgsConstructor
public class SearchKeywordRepositoryImpl implements SearchKeywordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchKeyword> getPopular() {
        QueryResults<SearchKeyword> results = queryFactory
                .selectFrom(searchKeyword1)
                .orderBy(searchKeyword1.count.desc())
                .limit(10)
                .fetchResults();
        return results.getResults();
    }

    @Override
    public SearchKeyword getKeywordByString(String keyword) {
        SearchKeyword results = queryFactory
                .selectFrom(searchKeyword1)
                .where(searchKeyword1.searchKeyword.eq(keyword))
                .fetchOne();
        return results;
    }
}
