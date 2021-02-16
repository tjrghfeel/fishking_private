package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.model.common.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.tobe.fishking.v2.entity.common.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewDto> getShipReviews(Long shipId, Pageable pageable) {
//        QueryResults<ReviewDto> results = queryFactory
//                .select()
//                .from(review)
//                .where(review.goods.ship.id.eq(shipId))
//                .orderBy(review.createdDate.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
        return null;
    }
}
