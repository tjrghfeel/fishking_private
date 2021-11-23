package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.model.police.PoliceGoodsResponse;
import com.tobe.fishking.v2.model.police.QPoliceGoodsResponse;
import com.tobe.fishking.v2.model.police.QRiderResponse;
import com.tobe.fishking.v2.model.police.RiderResponse;
import com.tobe.fishking.v2.model.response.GoodsSmallResponse;
import com.tobe.fishking.v2.model.response.QGoodsSmallResponse;
import com.tobe.fishking.v2.model.response.QUpdateGoodsResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.tobe.fishking.v2.entity.common.QLoveTo.loveTo;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QGoodsFishingDate.goodsFishingDate;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.entity.fishing.QOrders.orders;
import static com.tobe.fishking.v2.entity.fishing.QRealTimeVideo.realTimeVideo;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id, LocalDate date) {
        LocalDateTime now = LocalDateTime.now().plusMinutes(30L);
        String time = "0000";
        if (now.toLocalDate().equals(date)) {
            time = now.toLocalTime().format(DateTimeFormatter.ofPattern("HHmm"));
        }
        NumberPath<Integer> countAlias = Expressions.numberPath(Integer.class, "count");
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods,
                        goodsFishingDate.reservedNumber
                ))
                .from(goods).join(goodsFishingDate).on(goods.eq(goodsFishingDate.goods))
                .where(goods.ship.id.eq(ship_id),
                        goods.isUse.eq(true),
                        goodsFishingDate.fishingDate.eq(date),
                        goods.fishingStartTime.goe(time)
                )
                .fetchResults();
        return result.getResults();
    }

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id) {
        NumberPath<Integer> countAlias = Expressions.numberPath(Integer.class, "count");
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods
                ))
                .from(goods)
                .where(goods.ship.id.eq(ship_id), goods.isUse.eq(true))
                .fetchResults();
        return result.getResults();
    }

    @Override
    public List<GoodsSmallResponse> searchGoods(Long shipId, String keyword, String status) {
        List<GoodsSmallResponse> results = queryFactory
                .select(new QGoodsSmallResponse(
                        goods.id,
                        goods.name,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.minPersonnel,
                        goods.maxPersonnel,
                        goods.totalAmount,
                        goods.isUse,
                        goods.positionSelect,
                        goods.reserveType,
                        goods.extraRun,
                        ExpressionUtils.as(JPAExpressions.select(goodsFishingDate.fishingDate.max()).from(goodsFishingDate).where(goodsFishingDate.goods.eq(goods)), Expressions.datePath(LocalDate.class, "endDate"))
                ))
                .from(goods)
                .where(goods.ship.id.eq(shipId), goods.name.containsIgnoreCase(keyword), eqStatus(status))
                .orderBy(goods.createdDate.desc())
                .fetch();
        return results;
    }

    private BooleanExpression eqStatus(String status) {
        BooleanExpression expression;
        switch (status) {
            case "active":
                expression = goods.isUse.eq(true);
                break;
            case "inactive":
                expression = goods.isUse.eq(false);
                break;
            default:
                expression = null;
        }
        return expression;
    }

    @Override
    public UpdateGoodsResponse getGoodsData(Long goodsId) {
        UpdateGoodsResponse response = queryFactory
                .select(new QUpdateGoodsResponse(goods, goods.ship.id, goods.ship.shipName))
                .from(goods)
                .where(goods.id.eq(goodsId))
                .fetchOne();
        return response;
    }

    @Override
    public Long getTodayRunGoods() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.bookConfirm);
        statuses.add(OrderStatus.bookFix);
        statuses.add(OrderStatus.fishingComplete);
        return queryFactory
                .selectFrom(goods).join(orderDetails).on(goods.eq(orderDetails.goods))
                .where(goods.fishingDates.any().fishingDateString.eq(today), goods.isUse.eq(true))
                .groupBy(goods)
                .fetchCount();
    }

    @Override
    public Long getNowRunGoods() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.bookConfirm);
        statuses.add(OrderStatus.bookFix);
        statuses.add(OrderStatus.fishingComplete);
        return queryFactory
                .selectFrom(goods).join(orderDetails).on(goods.eq(orderDetails.goods))
                .where((goods.fishingDates.any().fishingDateString.eq(today).and(
                        Expressions.asTime(goods.fishingStartTime).before(time)).and(
                        Expressions.asTime(goods.fishingEndTime).after(time))),
                        (goods.fishingDates.any().fishingDateString.eq(yesterday).and(
                                goods.fishingEndDate.notEqualsIgnoreCase("")).and(
                                Expressions.asTime(goods.fishingEndTime).after(time))),
                        goods.isUse.eq(true))
                .groupBy(goods)
                .fetchCount();
    }

    @Override
    public Long getWaitRidePersonnel() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.bookConfirm);
        statuses.add(OrderStatus.bookFix);
        statuses.add(OrderStatus.fishingComplete);
        return queryFactory
                .select(rideShip)
                .from(rideShip).join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails)).join(goods).on(orderDetails.goods.eq(goods))
                .join(orders).on(orderDetails.orders.eq(orders))
                .where(goods.isUse.eq(true),
                        goods.fishingDates.any().fishingDateString.eq(today),
                        rideShip.isRide.eq(false),
                        orderDetails.orders.orderStatus.in(statuses),
                        orders.fishingDate.eq(today))
                .fetchCount();
    }

    @Override
    public Long getRealRidePersonnel() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.bookConfirm);
        statuses.add(OrderStatus.bookFix);
        statuses.add(OrderStatus.fishingComplete);
        return queryFactory
                .select(rideShip)
                .from(rideShip).join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails)).join(goods).on(orderDetails.goods.eq(goods))
                .join(orders).on(orderDetails.orders.eq(orders))
                .where(goods.isUse.eq(true),
                        goods.fishingDates.any().fishingDateString.eq(today),
                        rideShip.isRide.eq(true),
                        orderDetails.orders.orderStatus.in(statuses),
                        orders.fishingDate.eq(today))
                .fetchCount();
    }

    @Override
    public List<PoliceGoodsResponse> getPoliceAllGoods() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
        JPAQuery<PoliceGoodsResponse> query = queryFactory
                .select(new QPoliceGoodsResponse(
                        ship.id,
                        goods.id,
                        ship.shipName,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.fishingEndTime.substring(0,2).concat(":").concat(goods.fishingEndTime.substring(2,4)),
                        goods.fishingEndDate,
                        goods.maxPersonnel,
                        ExpressionUtils.as(JPAExpressions
                                .select(rideShip.count())
                                .from(rideShip)
                                .where(rideShip.isRide.eq(true), rideShip.ordersDetail.orders.eq(orders)),
                                Expressions.numberPath(Long.class, "ridePersonnel")),
                        ship.liveLocation.latitude,
                        ship.liveLocation.longitude,
                        ExpressionUtils.as(JPAExpressions
                                        .select(realTimeVideo.count())
                                        .from(realTimeVideo)
                                        .where(realTimeVideo.isUse.eq(true), realTimeVideo.ships.eq(ship)),
                                Expressions.numberPath(Long.class, "cameraCount"))
                ))
                .from(goods).join(ship).on(goods.ship.eq(ship)).join(orders).on(orders.goods.eq(goods))
                .where(goods.isUse.eq(true),
                        (goods.fishingDates.any().fishingDateString.eq(today).and(
                                Expressions.asTime(goods.fishingStartTime).before(time)).and(
                                Expressions.asTime(goods.fishingEndTime).after(time))),
                        goods.fishingDates.any().fishingDateString.eq(yesterday).and(
                                goods.fishingEndDate.notEqualsIgnoreCase("")).and(
                                Expressions.asTime(goods.fishingEndTime).after(time))
                )
                .groupBy(goods);
            return query.fetch();
    }

    @Override
    public Page<PoliceGoodsResponse> getPoliceGoods(Integer page) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
        JPAQuery<PoliceGoodsResponse> query = queryFactory
                .select(new QPoliceGoodsResponse(
                        ship.id,
                        goods.id,
                        ship.shipName,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.fishingEndTime.substring(0,2).concat(":").concat(goods.fishingEndTime.substring(2,4)),
                        goods.fishingEndDate,
                        goods.maxPersonnel,
                        ExpressionUtils.as(JPAExpressions
                                        .select(rideShip.count())
                                        .from(rideShip)
                                        .where(rideShip.isRide.eq(true), rideShip.ordersDetail.orders.eq(orders)),
                                Expressions.numberPath(Long.class, "ridePersonnel")),
                        ship.liveLocation.latitude,
                        ship.liveLocation.longitude,
                        ExpressionUtils.as(JPAExpressions
                                        .select(realTimeVideo.count())
                                        .from(realTimeVideo)
                                        .where(realTimeVideo.isUse.eq(true), realTimeVideo.ships.eq(ship)),
                                Expressions.numberPath(Long.class, "cameraCount"))
                ))
                .from(goods).join(ship).on(goods.ship.eq(ship)).join(orders).on(orders.goods.eq(goods))
                .where(goods.isUse.eq(true),
                        (goods.fishingDates.any().fishingDateString.eq(today).or(
                                goods.fishingDates.any().fishingDateString.eq(yesterday)).and(
                                        goods.fishingEndDate.notEqualsIgnoreCase(""))
                        )
                )
                .groupBy(goods);

        Pageable pageable = PageRequest.of(page, 20, Sort.by("fishingStartTime"));
        QueryResults<PoliceGoodsResponse> results = query.orderBy(goods.fishingStartTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<RiderResponse> getRiderData(Long goodsId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.bookConfirm);
        statuses.add(OrderStatus.bookFix);
        statuses.add(OrderStatus.fishingComplete);
        List<RiderResponse> responses = queryFactory
                .select(new QRiderResponse(
                        rideShip.name,
                        rideShip.birthday,
                        rideShip.phoneNumber,
                        rideShip.emergencyPhone,
                        rideShip.bFingerPrint
                ))
                .from(rideShip).join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails)).join(orders).on(orderDetails.orders.eq(orders)).join(goods).on(orders.goods.eq(goods))
                .where(orders.fishingDate.eq(today),
                        goods.id.eq(goodsId),
                        orders.orderStatus.in(statuses))
                .fetch();
        return responses;
    }

    @Override
    public List<Goods> getNeedConfirm(String date, String time) {
        List<Goods> responses = queryFactory
                .select(goods)
                .from(goods).join(ship).on(goods.ship.eq(ship)).join(goodsFishingDate).on(goods.eq(goodsFishingDate.goods))
                .where(ship.isActive.eq(true),
                        goods.fishingStartTime.substring(0,2).eq(time),
                        goodsFishingDate.fishingDateString.eq(date),
                        goods.isUse.eq(true),
                        JPAExpressions
                                .select(orders.count())
                                .from(orders)
                                .where(orders.goods.eq(goods), orders.fishingDate.eq(date)).gt(0L)
                )
                .fetch();
        return responses;
    }

    @Override
    public Page<Goods> getDayFishing(String date, Member member, Integer page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by("fishingStartTime"));

        QueryResults<Goods> results = queryFactory
                .select(goods)
                .from(goods).join(ship).on(goods.ship.eq(ship)).join(goodsFishingDate).on(goods.eq(goodsFishingDate.goods))
                .where(ship.isActive.eq(true),
                        ship.company.member.eq(member),
                        goodsFishingDate.fishingDateString.eq(date),
                        goods.isUse.eq(true))
                .orderBy(goods.fishingStartTime.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
