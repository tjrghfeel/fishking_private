package com.tobe.fishking.v2.repository.common;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.model.common.QReviewResponse;
import com.tobe.fishking.v2.model.common.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.tobe.fishking.v2.entity.QFileEntity.fileEntity;
import static com.tobe.fishking.v2.entity.common.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ReviewResponse> getShipReviews(Long shipId, Pageable pageable) {
        QueryResults<ReviewResponse> results = queryFactory
                .select(new QReviewResponse(
                        review,
                        review.goods,
                        review.goods.ship.id,
                        review.member
                ))
                .from(review)
                .where(review.goods.ship.id.eq(shipId),
                        review.isDeleted.eq(false))
                .orderBy(review.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
