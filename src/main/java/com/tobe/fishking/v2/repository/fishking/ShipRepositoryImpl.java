package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.model.response.QFishingShipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.dsl.MathExpressions.*;
import static com.tobe.fishking.v2.entity.common.QCommonCode.commonCode;
import static com.tobe.fishking.v2.entity.common.QObserverCode.observerCode;
import static com.tobe.fishking.v2.entity.common.QTake.take;
import static com.tobe.fishking.v2.entity.fishing.QCompany.company;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QOrders.orders;
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
        NumberPath<Double> aliasDistance = Expressions.numberPath(Double.class, "distance");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "popular":
                        OrderSpecifier<?> orderPopular = aliasLiked.desc();
                        ORDERS.add(orderPopular);
                        break;
                    case "distance":
//                        OrderSpecifier<?> orderDistance = getSortedColumn(Order.ASC, ship, "distance");
                        OrderSpecifier<?> orderDistance = aliasDistance.asc();
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
                        ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
                        ExpressionUtils.as(
                                acos(
                                    cos(radians(Expressions.constant(shipSearchDTO.getLatitude())))
                                    .multiply(cos(radians(ship.location.latitude)))
                                    .multiply(cos(radians(ship.location.longitude).subtract(radians(Expressions.constant(shipSearchDTO.getLongitude())))))
                                    .add(sin(radians(Expressions.constant(shipSearchDTO.getLatitude()))).multiply(sin(radians(ship.location.latitude))))
                                ).multiply(Expressions.constant(6371)), aliasDistance),
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
                        hasRealTimeVideos(shipSearchDTO.getHasRealTimeVideo()),
                        ship.isActive.eq(true)
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
                        ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
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
                        hasRealTimeVideos(shipSearchDTO.getHasRealTimeVideo()),
                        ship.isActive.eq(true)
                )
                .limit(100)
                .fetchResults();
        return results.getResults();
    }

    @Override
    public Page<ShipListResponse> searchMain(String keyword, String type, Double lat, Double lng, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");
        NumberPath<Integer> aliasSold = Expressions.numberPath(Integer.class, "sold");
        NumberPath<Long> aliasLiked = Expressions.numberPath(Long.class, "liked");
        NumberPath<Double> aliasDistance = Expressions.numberPath(Double.class, "distance");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "distance":
                        OrderSpecifier<?> orderDistance = aliasDistance.asc();
                        ORDERS.add(orderDistance);
                        break;
                    case "name":
                        OrderSpecifier<?> orderName = getSortedColumn(Order.ASC, ship, "shipName");
                        ORDERS.add(orderName);
                        break;
                }
            }
        }
        ORDERS.add(getSortedColumn(Order.DESC, ship, "createdDate"));

        QueryResults<ShipListResponse> results = queryFactory
                .select(Projections.constructor(ShipListResponse.class,
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                        ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
                        ExpressionUtils.as(
                                acos(
                                        cos(radians(Expressions.constant(lat)))
                                                .multiply(cos(radians(ship.location.latitude)))
                                                .multiply(cos(radians(ship.location.longitude).subtract(radians(Expressions.constant(lng)))))
                                                .add(sin(radians(Expressions.constant(lat))).multiply(sin(radians(ship.location.latitude))))
                                ).multiply(Expressions.constant(6371)), aliasDistance),
                        ship
                ))
                .from(ship)
                .where(ship.isActive.eq(true).and(
                        ship.fishSpecies.any().codeName.containsIgnoreCase(keyword)
                        .or(ship.shipName.containsIgnoreCase(keyword))
                        .or(ship.sido.concat(" ").concat(ship.sigungu).containsIgnoreCase(keyword))
                    ).and(searchLive(type.equals("live")))
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<ShipListResponse> searchMainWithType(String keyword, String type, Double lat, Double lng, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");
        NumberPath<Integer> aliasSold = Expressions.numberPath(Integer.class, "sold");
        NumberPath<Long> aliasLiked = Expressions.numberPath(Long.class, "liked");
        NumberPath<Double> aliasDistance = Expressions.numberPath(Double.class, "distance");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "distance":
                        OrderSpecifier<?> orderDistance = aliasDistance.asc();
                        ORDERS.add(orderDistance);
                        break;
                    case "name":
                        OrderSpecifier<?> orderName = getSortedColumn(Order.ASC, ship, "shipName");
                        ORDERS.add(orderName);
                        break;
                }
            }
        }
        ORDERS.add(getSortedColumn(Order.DESC, ship, "createdDate"));

        if (type.equals("direction")) {
            QueryResults<ShipListResponse> results = queryFactory
                    .select(Projections.constructor(ShipListResponse.class,
                            ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                            ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                            ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
                            ExpressionUtils.as(
                                    acos(
                                            cos(radians(Expressions.constant(lat)))
                                                    .multiply(cos(radians(ship.location.latitude)))
                                                    .multiply(cos(radians(ship.location.longitude).subtract(radians(Expressions.constant(lng)))))
                                                    .add(sin(radians(Expressions.constant(lat))).multiply(sin(radians(ship.location.latitude))))
                                    ).multiply(Expressions.constant(6371)), aliasDistance),
                            ship
                    ))
                    .from(ship)
                    .where(ship.isActive.eq(true)
                            .and(ship.observerCode.in(
                                    JPAExpressions
                                            .select(observerCode.code)
                                            .from(observerCode).join(commonCode).on(observerCode.forecastCode.eq(commonCode.code))
                                            .where(commonCode.codeName.eq(keyword))
                            ))
                    )
                    .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            return new PageImpl<>(results.getResults(), pageable, results.getTotal());
        } else {
            QueryResults<ShipListResponse> results = queryFactory
                    .select(Projections.constructor(ShipListResponse.class,
                            ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                            ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                            ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
                            ExpressionUtils.as(
                                    acos(
                                            cos(radians(Expressions.constant(lat)))
                                                    .multiply(cos(radians(ship.location.latitude)))
                                                    .multiply(cos(radians(ship.location.longitude).subtract(radians(Expressions.constant(lng)))))
                                                    .add(sin(radians(Expressions.constant(lat))).multiply(sin(radians(ship.location.latitude))))
                                    ).multiply(Expressions.constant(6371)), aliasDistance),
                            ship
                    ))
                    .from(ship)
                    .where(ship.isActive.eq(true)
                            .and(ship.fishSpecies.any().codeName.eq(keyword))
                    )
                    .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            return new PageImpl<>(results.getResults(), pageable, results.getTotal());
        }
    }

    @Override
    public Page<TvListResponse> searchTvList(ShipSearchDTO shipSearchDTO, Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        NumberPath<Integer> aliasPrice = Expressions.numberPath(Integer.class, "price");
        NumberPath<Integer> aliasSold = Expressions.numberPath(Integer.class, "sold");
        NumberPath<Long> aliasLiked = Expressions.numberPath(Long.class, "liked");
        NumberPath<Double> aliasDistance = Expressions.numberPath(Double.class, "distance");

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order: pageable.getSort()) {
                switch (order.getProperty()) {
                    case "popular":
                        OrderSpecifier<?> orderPopular = aliasLiked.desc();
                        ORDERS.add(orderPopular);
                        break;
                    case "distance":
//                        OrderSpecifier<?> orderDistance = getSortedColumn(Order.ASC, ship, "distance");
                        OrderSpecifier<?> orderDistance = aliasDistance.asc();
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

        QueryResults<TvListResponse> results = queryFactory
                .select(new QTvListResponse(
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasPrice),
                        ExpressionUtils.as(JPAExpressions.select(goods.totalAmount.min()).from(goods).where(goods.ship.id.eq(ship.id)), aliasSold),
                        ExpressionUtils.as(JPAExpressions.select(take.count()).from(take).where(take.linkId.eq(ship.id), take.takeType.eq(TakeType.ship)), aliasLiked),
                        ExpressionUtils.as(
                                acos(
                                        cos(radians(Expressions.constant(shipSearchDTO.getLatitude())))
                                                .multiply(cos(radians(ship.location.latitude)))
                                                .multiply(cos(radians(ship.location.longitude).subtract(radians(Expressions.constant(shipSearchDTO.getLongitude())))))
                                                .add(sin(radians(Expressions.constant(shipSearchDTO.getLatitude()))).multiply(sin(radians(ship.location.latitude))))
                                ).multiply(Expressions.constant(6371)), aliasDistance),
                        ship
                ))
                .from(ship)
                .where(inSpecies(shipSearchDTO.getSpeciesList()),
                        inFishingDate(shipSearchDTO.getFishingDate()),
                        eqSido(shipSearchDTO.getSido()),
                        eqSigungu(shipSearchDTO.getSigungu()),
                        inGenres(shipSearchDTO.getGenresList()),
                        inServices(shipSearchDTO.getServicesList()),
                        inFacilities(shipSearchDTO.getFacilitiesList()),
                        hasRealTimeVideos(true),
                        ship.isActive.eq(true)
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//        return null;
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

    private BooleanExpression searchLive(Boolean searchLive) {
        if (searchLive == null) {
            return null;
        } else {
            if (searchLive) {
                return ship.shiipRealTimeVideos.size().gt(0);
            } else {
                return ship.shiipRealTimeVideos.size().gt(-1);
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

    @Override
    public Page<FishingShipResponse> getShipsByCompanyMember(Long memberId, String keywordType, String keyword, String status, Pageable pageable) {
        QueryResults<FishingShipResponse> results = queryFactory
                .select(new QFishingShipResponse(ship))
                .from(ship).join(company).on(ship.company.eq(company))
                .where(company.member.id.eq(memberId), containKeyword(keywordType, keyword), eqGoodStatus(status))
                .orderBy(ship.shipName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<FishingShipResponse> getShipsByCompanyMember2(Long memberId, String keyword, String cameraActive, Pageable pageable) {
        QueryResults<FishingShipResponse> results = queryFactory
                .select(new QFishingShipResponse(ship))
                .from(ship).join(company).on(ship.company.eq(company))
                .where(company.member.id.eq(memberId), containKeyword("shipName", keyword), cameraOn(cameraActive))
                .orderBy(ship.shipName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression cameraOn(String cameraActive) {
        BooleanExpression expression;
        switch (cameraActive) {
            case "true":
                expression = ship.shiipRealTimeVideos.any().isUse.eq(true);
                break;
            case "false":
                expression = ship.shiipRealTimeVideos.any().isUse.eq(false);
                break;
            default:
                expression = null;
        }
        return expression;
    }

    private BooleanExpression containKeyword(String keywordType, String keyword) {
        BooleanExpression expression;
        switch (keywordType) {
            case "goodsName":
                expression = ship.goods.any().name.containsIgnoreCase(keyword);
                break;
            case "shipName":
                expression = ship.shipName.containsIgnoreCase(keyword);
                break;
            default:
                expression = null;
        }
        return expression;
    }

    private BooleanExpression eqGoodStatus(String status) {
        BooleanExpression expression;
        switch (status) {
            case "active":
                expression = ship.goods.any().isUse.eq(true);
                break;
            case "inactive":
                expression = ship.goods.any().isUse.eq(false);
                break;
            default:
                expression = null;
        }
        return expression;
    }
}
