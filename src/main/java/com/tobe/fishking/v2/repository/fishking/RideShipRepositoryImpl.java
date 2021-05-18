package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.RiderFingerPrint;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.smartsail.*;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tobe.fishking.v2.entity.fishing.QCompany.company;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QOrders.orders;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.entity.fishing.QRiderFingerPrint.riderFingerPrint;
import static com.tobe.fishking.v2.utils.QueryDslUtil.getSortedColumn;

@RequiredArgsConstructor
public class RideShipRepositoryImpl implements RideShipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TodayBoardingResponse> getTodayRiders(Long memberId, String orderBy, Boolean comp) {
        String fromDate = LocalDateTime.now().minusHours(6L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        String toDate = LocalDateTime.now().plusHours(6L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));

        OrderSpecifier<?> order;

        switch (orderBy) {
            case "shipName":
                order = getSortedColumn(Order.ASC, ship, "shipName");
                break;
            case "username":
                order = getSortedColumn(Order.ASC, rideShip, "name");
                break;
            default:
                order = getSortedColumn(Order.DESC, rideShip, "createdDate");

        }

        List<TodayBoardingResponse> responses = queryFactory
                .select(new QTodayBoardingResponse(
                        rideShip.id,
                        rideShip.name,
                        ship.shipName,
                        goods.name,
                        orders.fishingDate,
                        goods.fishingStartTime,
                        goods.fishingEndTime,
                        rideShip.phoneNumber,
                        rideShip.emergencyPhone,
                        rideShip.isRide
                ))
                .from(rideShip)
                    .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                    .join(orders).on(orderDetails.orders.eq(orders))
                    .join(goods).on(orderDetails.goods.eq(goods))
                    .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
                        isComplete(comp),
                        (new CaseBuilder().when(goods.fishingEndTime.eq("2400"))
                                .then(Expressions.dateTimeTemplate(LocalDateTime.class, "ADDDATE({0}, 1)", (Expressions.dateTimeTemplate(LocalDateTime.class, "STR_TO_DATE({0}, '%Y-%m-%d%H%i')",
                                        orders.fishingDate.concat("0000")
                                ))))
                                .otherwise(Expressions.dateTimeTemplate(LocalDateTime.class, "STR_TO_DATE({0}, '%Y-%m-%d%H%i')",
                                        orders.fishingDate.concat(goods.fishingEndTime)
                                ))
//                        .goe(fromDate
                                .after(Expressions.dateTimeTemplate(LocalDateTime.class, "STR_TO_DATE({0}, '%Y-%m-%d %H%i')", fromDate)
                        )),
                        Expressions.dateTimeTemplate(LocalDateTime.class, "STR_TO_DATE({0}, '%Y-%m-%d%H%i')",
                                orders.fishingDate.concat(goods.fishingStartTime)
                        ).before(Expressions.dateTimeTemplate(LocalDateTime.class, "STR_TO_DATE({0}, '%Y-%m-%d %H%i')", toDate))
                )
                .orderBy(order).fetch();
        return responses;
    }

    private BooleanExpression isComplete(Boolean comp) {
        if (comp) {
            return orders.orderStatus.eq(OrderStatus.fishingComplete);
        } else {
            return orders.orderStatus.eq(OrderStatus.bookFix);
        }
    }

    public Map<String, Object> dashboard(Long memberId) {
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));

        Long cancelCount = queryFactory
                .selectFrom(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookCancel),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long failCount = queryFactory
                .select()
                .from(rideShip)
                    .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                    .join(goods).on(orderDetails.goods.eq(goods))
                    .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
                        goods.fishingStartTime.lt(time),
                        rideShip.isRide.eq(false),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long confirmCount = queryFactory
                .select()
                .from(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
//                        goods.fishingStartTime.lt(time),
                        rideShip.isRide.eq(true),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long waitCount = queryFactory
                .select()
                .from(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
//                        goods.fishingStartTime.gt(time),
                        rideShip.isRide.eq(false),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Map<String, Object> response = new HashMap<>();
        response.put("failCount", failCount);
        response.put("waitCount", waitCount);
        response.put("confirmCount", confirmCount);
        response.put("cancelCount", cancelCount);
        if (confirmCount + failCount == 0) {
            response.put("confirmPercentage", 0);
            response.put("failPercentage", 0);
        } else {
            response.put("confirmPercentage", confirmCount * 100.0 / (confirmCount + failCount));
            response.put("failPercentage", failCount * 100.0 / (confirmCount + failCount));
        }
        return response;
    }

    @Override
    public RiderFingerPrint getFingerPrint(String name, String phone) {
        return queryFactory
                .selectFrom(riderFingerPrint)
                .where(riderFingerPrint.name.eq(name),
                        riderFingerPrint.phone.eq(phone))
                .fetchOne();
    }

    @Override
    public Page<RiderGoodsListResponse> searchRiders(Long memberId, RiderSearchDTO dto, Pageable pageable) {
        QueryResults<RiderGoodsListResponse> results = queryFactory
                .select(new QRiderGoodsListResponse(
                        orders.id,
                        ship.shipName,
                        ship.profileImage,
                        goods.name,
                        orders.orderStatus,
                        orders.fishingDate,
                        goods.fishingStartTime,
                        orders.orderNumber,
                        orders.createdBy.memberName,
                        orders.createdBy.email,
                        orderDetails.personnel
                ))
                .from(orders).join(orderDetails).on(orderDetails.orders.eq(orders))
                .join(goods).on(orders.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(searchRiderKeyword(dto.getKeywordType(), dto.getKeyword()),
                        searchRiderStatus(dto.getStatus()),
                        orders.fishingDate.eq(dto.getStartDate()).or(orders.fishingDate.eq(dto.getEndDate())).or(orders.fishingDate.between(dto.getStartDate(), dto.getEndDate())),
                        ship.company.member.id.eq(memberId))
                .orderBy(orders.fishingDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanExpression searchRiderStatus(String status){
        BooleanExpression expression;
        switch (status) {
            case "wait":
                List<OrderStatus> statuses = new ArrayList<>();
                statuses.add(OrderStatus.bookFix);
                statuses.add(OrderStatus.bookConfirm);
                statuses.add(OrderStatus.waitBook);
                statuses.add(OrderStatus.bookRunning);
                expression = orders.orderStatus.in(statuses);
                break;
            case "complete":
                expression = orders.orderStatus.eq(OrderStatus.fishingComplete);
                break;
            case "cancel":
                expression = orders.orderStatus.eq(OrderStatus.bookCancel);
                break;
            default:
                expression = orders.orderStatus.ne(OrderStatus.book);
        }
        return expression;
    }

    private BooleanExpression searchRiderKeyword(String keywordType, String keyword) {
        BooleanExpression expression;
        switch (keywordType) {
            case "username":
                expression = orders.createdBy.memberName.containsIgnoreCase(keyword);
                break;
            case "riderName":
                expression = orders.id.in(JPAExpressions
                        .select(orders.id)
                        .from(orders).join(orderDetails).on(orderDetails.orders.eq(orders))
                        .join(rideShip).on(rideShip.ordersDetail.eq(orderDetails))
                        .where(rideShip.name.containsIgnoreCase(keyword)));
                break;
            default:
                expression = null;
        }
        return expression;
    }

    @Override
    public List<Tuple> getDetailRiders(Long orderId) {
        return queryFactory
                .select(rideShip.id, rideShip.name, rideShip.phoneNumber, rideShip.emergencyPhone, rideShip.isRide, rideShip.bFingerPrint)
                .from(rideShip).join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .where(orderDetails.orders.id.eq(orderId))
                .fetch();
    }

    public Map<String, Object> dashboardForManage() {
        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));

        Long cancelCount = queryFactory
                .selectFrom(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookCancel),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long failCount = queryFactory
                .select()
                .from(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(
                        goods.fishingStartTime.lt(time),
                        rideShip.isRide.eq(false),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long confirmCount = queryFactory
                .select()
                .from(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(
//                        goods.fishingStartTime.lt(time),
                        rideShip.isRide.eq(true),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Long waitCount = queryFactory
                .select()
                .from(rideShip)
                .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                .join(goods).on(orderDetails.goods.eq(goods))
                .join(ship).on(goods.ship.eq(ship))
                .where(
//                        goods.fishingStartTime.gt(time),
                        rideShip.isRide.eq(false),
//                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix).or(orderDetails.orders.orderStatus.eq(OrderStatus.fishingComplete)),
                        orderDetails.orders.orderStatus.eq(OrderStatus.bookFix),
                        orderDetails.orders.fishingDate.eq(now))
                .fetchCount();

        Map<String, Object> response = new HashMap<>();
        response.put("failCount", failCount);
        response.put("waitCount", waitCount);
        response.put("confirmCount", confirmCount);
        response.put("cancelCount", cancelCount);
        if (confirmCount + failCount == 0) {
            response.put("confirmPercentage", 0);
            response.put("failPercentage", 0);
        } else {
            response.put("confirmPercentage", confirmCount * 100.0 / (confirmCount + failCount));
            response.put("failPercentage", failCount * 100.0 / (confirmCount + failCount));
        }
        return response;
    }
}
