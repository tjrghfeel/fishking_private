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
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.fishing.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.tobe.fishking.v2.entity.common.QLoveTo.loveTo;
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
        NumberPath<Long> aliasLiked = Expressions.numberPath(Long.class, "liked");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "popular":
                        OrderSpecifier<?> orderPopular = aliasLiked.desc();
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
                        ExpressionUtils.as(JPAExpressions.select(loveTo.count()).from(loveTo).where(loveTo.linkId.eq(ship.id), loveTo.takeType.eq(TakeType.ship)), aliasLiked),
                        ship
                ))
                .from(ship)
                .where(eqFishingType(shipSearchDTO.getFishingType()),
                        inSpecies(shipSearchDTO.getSpeciesList()),
                        inFishingDate(shipSearchDTO.getFishingDate()),
                        eqSido(shipSearchDTO.getSido()),
                        eqSigungu(shipSearchDTO.getSigungu()),
                        inGenres(shipSearchDTO.getGenresList()),
                        inServices(shipSearchDTO.getServicesList()),
                        inFacilities(shipSearchDTO.getFacilitiesList()),
                        hasRealTimeVideos(shipSearchDTO.getHasRealTimeVideo())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<ShipListResponse> searchAllForMap(ShipSearchDTO shipSearchDTO) {
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");
        NumberPath<Integer> aliasSold = Expressions.numberPath(Integer.class, "sold");
        NumberPath<Long> aliasLiked = Expressions.numberPath(Long.class, "liked");

        QueryResults<ShipListResponse> results = queryFactory
                .select(Projections.constructor(ShipListResponse.class,
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                        ExpressionUtils.as(JPAExpressions.select(loveTo.count()).from(loveTo).where(loveTo.linkId.eq(ship.id), loveTo.takeType.eq(TakeType.ship)), aliasLiked),
                        ship
                ))
                .from(ship)
                .where(eqFishingType(shipSearchDTO.getFishingType()),
                        inSpecies(shipSearchDTO.getSpeciesList()),
                        inFishingDate(shipSearchDTO.getFishingDate()),
                        eqSido(shipSearchDTO.getSido()),
                        eqSigungu(shipSearchDTO.getSigungu()),
                        inGenres(shipSearchDTO.getGenresList()),
                        inServices(shipSearchDTO.getServicesList()),
                        inFacilities(shipSearchDTO.getFacilitiesList()),
                        hasRealTimeVideos(shipSearchDTO.getHasRealTimeVideo())
                )
                .fetchResults();
        return results.getResults();
    }

    private BooleanExpression eqFishingType(String fishingType) {
        return fishingType.isEmpty() ? null : ship.fishingType.eq(FishingType.valueOf(fishingType));
    }

    private BooleanExpression inSpecies(List<String> species) {
        return species.isEmpty() ? null : ship.fishSpecies.any().code.in(species);
    }

    private BooleanExpression inFishingDate(String fishingDate) {
        return fishingDate.isEmpty() ? null : ship.goods.any().fishingDates.any().fishingDateString.eq(fishingDate);
    }

    private BooleanExpression eqSido(String sido) {
        return sido.isEmpty() ? null : ship.sido.containsIgnoreCase(sido);
    }

    private BooleanExpression eqSigungu(String sigungu) {
        return sigungu.isEmpty() ? null : ship.sigungu.containsIgnoreCase(sigungu);
    }

    private BooleanExpression inGenres(List<String> genres) {
        return genres.isEmpty() ? null : ship.goods.any().genres.any().code.in(genres);
    }

    private BooleanExpression inServices(List<String> services) {
        return services.isEmpty() ? null : ship.services.any().code.in(services);
    }

    private BooleanExpression inFacilities(List<String> facilities) {
        return facilities.isEmpty() ? null : ship.facilities.any().code.in(facilities);
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

    @Override
    public ShipResponse getDetail(Long ship_id) {
        ShipResponse result = queryFactory
                .select(new QShipResponse(
                        ship
                ))
                .from(ship)
                .where(ship.id.eq(ship_id))
                .fetchOne();
        return result;
    }
}
