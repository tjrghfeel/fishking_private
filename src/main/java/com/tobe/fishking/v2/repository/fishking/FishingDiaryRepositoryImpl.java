package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.model.board.FishingDiaryMainResponse;
import com.tobe.fishking.v2.model.board.FishingDiarySearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.tobe.fishking.v2.entity.QFileEntity.fileEntity;
import static com.tobe.fishking.v2.entity.board.QComment.comment;
import static com.tobe.fishking.v2.entity.common.QLoveTo.loveTo;
import static com.tobe.fishking.v2.entity.fishing.QFishingDiary.fishingDiary;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.utils.QueryDslUtil.getSortedColumn;

@RequiredArgsConstructor
public class FishingDiaryRepositoryImpl implements FishingDiaryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FishingDiary> getDiaryByShipId(Long ship_id) {
        List<FishingDiary> results = queryFactory
                .selectFrom(fishingDiary)
                .where(fishingDiary.ship.id.eq(ship_id),
                        fishingDiary.filePublish.eq(FilePublish.fishingDiary),
                        fishingDiary.createdBy.isActive.eq(true),
                        fishingDiary.isActive.eq(true),
                        fishingDiary.isDeleted.eq(false)
                )
                .orderBy(fishingDiary.createdDate.desc())
                .fetch();
        return results;
    }

    @Override
    public List<FishingDiary> getBlogByShipId(Long ship_id) {
        List<FishingDiary> results = queryFactory
                .selectFrom(fishingDiary)
                .where(fishingDiary.ship.id.eq(ship_id),
                        fishingDiary.filePublish.eq(FilePublish.fishingBlog),
                        fishingDiary.createdBy.isActive.eq(true),
                        fishingDiary.isActive.eq(true),
                        fishingDiary.isDeleted.eq(false)
                )
                .orderBy(fishingDiary.createdDate.desc())
                .fetch();
        return results;
    }

    @Override
    public List<FishingDiaryMainResponse> getMainDiaries() {
        List<FishingDiaryMainResponse> results = queryFactory
                .select(Projections.constructor(FishingDiaryMainResponse.class,
                        fishingDiary
                ))
                .from(fishingDiary)
                .where(fishingDiary.filePublish.eq(FilePublish.fishingDiary),
                        fishingDiary.isDeleted.eq(false),
                        fishingDiary.isActive.eq(true),
                        fishingDiary.isHidden.eq(false),
                        fishingDiary.member.isActive.eq(true)
                )
                .orderBy(fishingDiary.createdDate.desc())
                .limit(10)
                .fetch();
        return results;
    }

    @Override
    public Page<FishingDiary> searchDiary(String keyword, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        ORDERS.add(getSortedColumn(Order.DESC, fishingDiary, "createdDate"));

        QueryResults<FishingDiary> results = queryFactory
                .selectFrom(fishingDiary)
                .where(fishingDiary.filePublish.eq(FilePublish.fishingDiary)
                        .and(fishingDiary.title.containsIgnoreCase(keyword)
                                .or(fishingDiary.contents.containsIgnoreCase(keyword)))
                        .and(fishingDiary.isDeleted.eq(false))
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<FishingDiarySearchResponse> searchDiaryOrBlog(String keyword, String type, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        StringPath aliasString = Expressions.stringPath("fileUrl");
        NumberPath<Long> aliasLoves = Expressions.numberPath(Long.class, "loves");
        NumberPath<Long> aliasComments = Expressions.numberPath(Long.class, "comments");
        for (Sort.Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "comments":
                    OrderSpecifier<?> orderComment = aliasComments.desc();
                    ORDERS.add(orderComment);
                    break;
                case "loves":
                    OrderSpecifier<?> orderLoves = aliasLoves.desc();
                    ORDERS.add(orderLoves);
                    break;
            }
        }
        ORDERS.add(getSortedColumn(Order.DESC, fishingDiary, "createdDate"));

        QueryResults<FishingDiarySearchResponse> results = queryFactory
                .select(Projections.constructor(FishingDiarySearchResponse.class,
                        fishingDiary.id,
                        fishingDiary.title,
                        fishingDiary.contents,
                        ExpressionUtils.as(JPAExpressions
                                .select(Expressions.asString("https://www.fishkingapp.com/resource/")
                                        .concat(fileEntity.fileUrl)
                                        .concat("/")
                                        .concat(fileEntity.storedFile)
                                        .concat("^")
                                        .concat(fileEntity.id.max().stringValue()))
                                .from(fileEntity)
                                .where(fileEntity.isDelete.eq(false),
                                        fileEntity.fileType.eq(FileType.image),
                                        typeCheckFile(type),
                                        fileEntity.pid.eq(fishingDiary.id))
                                .orderBy(fileEntity.isRepresent.desc())
                                , aliasString
                        ),
                        fishingDiary.createdBy.nickName,
                        fishingDiary.createdBy.profileImage,
                        fishingDiary.createdDate,
                        ExpressionUtils.as(JPAExpressions.select(loveTo.count()).from(loveTo).where(loveTo.linkId.eq(fishingDiary.id), loveTo.takeType.eq(getTakeType(type))), aliasLoves),
                        ExpressionUtils.as(JPAExpressions.select(comment.count()).from(comment).where(comment.linkId.eq(fishingDiary.id), comment.dependentType.eq(getDependentType(type))), aliasComments),
                        fishingDiary.ship.fishingType,
                        fishingDiary.fishingSpeciesName
                ))
                .from(fishingDiary)
                .where(typeCheck(type)
                        .and(fishingDiary.title.containsIgnoreCase(keyword)
                                .or(fishingDiary.contents.containsIgnoreCase(keyword)))
                        .and(fishingDiary.isDeleted.eq(false))
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression typeCheck(String type) {
        BooleanExpression expression = null;
        if (type != null) {
            if (type.equals("blog")) {
                expression = fishingDiary.filePublish.eq(FilePublish.fishingBlog);
            } else if (type.equals("diary")) {
                expression = fishingDiary.filePublish.eq(FilePublish.fishingDiary);
            }
        }
        return expression;
    }

    private BooleanExpression typeCheckFile(String type) {
        BooleanExpression expression = null;
        if (type != null) {
            if (type.equals("blog")) {
                expression = fileEntity.filePublish.eq(FilePublish.fishingBlog);
            } else if (type.equals("diary")) {
                expression = fileEntity.filePublish.eq(FilePublish.fishingDiary);
            }
        }
        return expression;
    }

    private TakeType getTakeType(String type) {
        if (type.equals("blog")) {
            return TakeType.fishingBlog;
        } else if (type.equals("diary")) {
            return TakeType.fishingDiary;
        } else {
            return TakeType.fishingDiary;
        }
    }

    private DependentType getDependentType(String type) {
        if (type.equals("blog")) {
            return DependentType.fishingBlog;
        } else if (type.equals("diary")) {
            return DependentType.fishingDiary;
        } else {
            return DependentType.fishingDiary;
        }
    }

}
