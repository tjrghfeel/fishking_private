package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.tobe.fishking.v2.entity.common.QCommonCode.commonCode;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.utils.QueryDslUtil.getSortedColumn;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class ShipRepositoryImpl implements ShipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ShipListResponse> searchAll(ShipSearchDTO shipSearchDTO, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");
        NumberPath<Integer> aliasSold = Expressions.numberPath(Integer.class, "sold");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "popular":
                        OrderSpecifier<?> orderPopular = getSortedColumn(Order.DESC, ship, "reviewCount");
                        ORDERS.add(orderPopular);
                        break;
                    case "distance":
                        OrderSpecifier<?> orderDistance = getSortedColumn(Order.ASC, ship, "distance");
                        ORDERS.add(orderDistance);
                        break;
                    case "lowPrice":
                        OrderSpecifier<?> orderLowPrice = aliasPrice.asc();
                        ORDERS.add(orderLowPrice);
                        break;
                    case "highPrice":
                        OrderSpecifier<?> orderHighPrice = aliasPrice.desc();
                        ORDERS.add(orderHighPrice);
                        break;
                    case "review":
                        OrderSpecifier<?> orderReview = getSortedColumn(Order.DESC, ship, "reviewCount");
                        ORDERS.add(orderReview);
                        break;
                    case "sold":
                        OrderSpecifier<?> orderSold = aliasSold.desc();
//                        ORDERS.add(orderSold);
                        break;
                }
            }
        }
        ORDERS.add(getSortedColumn(Order.DESC, ship, "createdDate"));

        QueryResults<ShipListResponse> results = queryFactory
                .select(Projections.constructor(ShipListResponse.class,
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                        ship
                ))
                .from(ship)
                .where(eqFishingType(shipSearchDTO.getFishingType()),
                        inSpecies(shipSearchDTO.getSpecies()),
                        inFishingDate(shipSearchDTO.getFishingDate()),
                        eqSido(shipSearchDTO.getSido()),
                        inGenres(shipSearchDTO.getGenres()),
                        inServices(shipSearchDTO.getServices()),
                        inFacilities(shipSearchDTO.getFacilities()),
                        hasRealTimeVideos(shipSearchDTO.getHasRealTimeVideo())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression eqFishingType(String fishingType) {
        return fishingType.isEmpty() ? null : ship.fishingType.eq(FishingType.valueOf(fishingType));
    }

    private BooleanExpression inSpecies(List<String> species) {
        return species.isEmpty() ? null : ship.fishSpecies.any().codeName.in(species);
    }

    private BooleanExpression inFishingDate(String fishingDate) {
        return fishingDate.isEmpty() ? null : ship.goods.any().fishingDates.any().fishingDateString.eq(fishingDate);
    }

    private BooleanExpression eqSido(String sido) {
        return sido.isEmpty() ? null : ship.sido.containsIgnoreCase(sido);
    }

    private BooleanExpression inGenres(List<String> genres) {
        return genres.isEmpty() ? null : ship.goods.any().genres.any().codeName.in(genres);
    }

    private BooleanExpression inServices(List<String> services) {
        return services.isEmpty() ? null : ship.services.any().codeName.in(services);
    }

    private BooleanExpression inFacilities(List<String> facilities) {
        return facilities.isEmpty() ? null : ship.facilities.any().codeName.in(facilities);
    }

    private BooleanExpression hasRealTimeVideos(Boolean hasRealTimeVideo) {
        if (hasRealTimeVideo == null) {
            return null;
        } else {
            if (hasRealTimeVideo) {
                return ship.shiipRealTimeVideos.size().gt(0);
            } else {
                return ship.shiipRealTimeVideos.size().eq(0);
            }
        }
    }
}
