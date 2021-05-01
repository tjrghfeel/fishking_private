package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.*;
import com.tobe.fishking.v2.utils.DateUtils;
import javassist.expr.Expr;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tobe.fishking.v2.entity.fishing.QCompany.company;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QOrders.orders;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class OrdersRepositoryImpl implements OrdersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Orders> getCheckOrders() {

        return null;
    }

    @Override
    public Page<OrderListResponse> searchOrders(SearchOrdersDTO dto, Long memberId, Pageable pageable, String keys) {
        QueryResults<OrderListResponse> results = queryFactory
                .select(new QOrderListResponse(
                        orders.id,
                        ship.shipName,
                        goods.name,
                        orders.orderNumber,
                        orders.fishingDate,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.fishingEndTime.substring(0,2).concat(":").concat(goods.fishingEndTime.substring(2,4)),
                        orders.createdDate,
                        orderDetails.personnel,
//                        ExpressionUtils.as(JPAExpressions.select(rideShip.count()).from(rideShip).where(rideShip.ordersDetail.orders.eq(orders)), Expressions.numberPath(Long.class, "personnel")),
                        orders.totalAmount,
                        orders.orderStatus,
                        orders.createdBy.profileImage,
                        orders.createdBy.memberName
                ))
                .from(orders).join(goods).on(orders.goods.eq(goods)).join(ship).on(goods.ship.eq(ship)).join(company).on(ship.company.eq(company)).join(orderDetails).on(orderDetails.orders.eq(orders))
                .where(
                        eqStatus(dto.getStatus()),
                        eqPayMethod(dto.getPayMethod()),
                        containsKeyword(dto.keywordType, dto.getKeyword(), keys),
                        betweenDate(dto.getStartDate(), dto.getEndDate()),
                        orders.isPay.eq(true),
                        company.member.id.eq(memberId)
                )
                .orderBy(orders.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
//        return null;
    }

    @Override
    public List<OrderListResponse> getBookRunning(Long memberId) {
        List<OrderListResponse> response = queryFactory
                .select(new QOrderListResponse(
                        orders.id,
                        ship.shipName,
                        goods.name,
                        orders.orderNumber,
                        orders.fishingDate,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.fishingEndTime.substring(0,2).concat(":").concat(goods.fishingEndTime.substring(2,4)),
                        orders.createdDate,
                        orderDetails.personnel,
                        orders.totalAmount,
                        orders.orderStatus,
                        orders.createdBy.profileImage,
                        orders.createdBy.memberName
                ))
                .from(orders).join(goods).on(orders.goods.eq(goods)).join(ship).on(goods.ship.eq(ship)).join(company).on(ship.company.eq(company)).join(orderDetails).on(orderDetails.orders.eq(orders))
                .where(
                        orders.orderStatus.eq(OrderStatus.bookRunning),
                        orders.isPay.eq(true),
                        company.member.id.eq(memberId)
                )
                .orderBy(Expressions.asDate(orders.fishingDate).asc())
                .limit(5)
                .fetch();
        return response;
    }

    @Override
    public List<OrderListResponse> getBookConfirm(Long memberId) {
        List<OrderListResponse> response = queryFactory
                .select(new QOrderListResponse(
                        orders.id,
                        ship.shipName,
                        goods.name,
                        orders.orderNumber,
                        orders.fishingDate,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.fishingEndTime.substring(0,2).concat(":").concat(goods.fishingEndTime.substring(2,4)),
                        orders.createdDate,
                        orderDetails.personnel,
                        orders.totalAmount,
                        orders.orderStatus,
                        orders.createdBy.profileImage,
                        orders.createdBy.memberName
                ))
                .from(orders).join(goods).on(orders.goods.eq(goods)).join(ship).on(goods.ship.eq(ship)).join(company).on(ship.company.eq(company)).join(orderDetails).on(orderDetails.orders.eq(orders))
                .where(
                        orders.orderStatus.eq(OrderStatus.bookConfirm),
                        orders.isPay.eq(true),
                        company.member.id.eq(memberId)
                )
                .orderBy(orders.createdDate.desc())
                .limit(5)
                .fetch();
        return response;
    }

    @Override
    public List<Tuple> getStatus(Long memberId) {
        List<Tuple> response = queryFactory
                .select(
                        Expressions.as(orders.orderStatus, "status"),
                        Expressions.as(orders.id.count(), "count")
                )
                .from(orders)
                .where(
                        orders.id.in(
                                JPAExpressions
                                        .select(orders.id)
                                        .from(orders).join(goods).on(orders.goods.eq(goods)).join(ship).on(goods.ship.eq(ship)).join(company).on(ship.company.eq(company))
                                        .where(orders.isPay.eq(true), company.member.id.eq(memberId))
                        )
                )
                .groupBy(orders.orderStatus)
                .fetch();
        return response;
    }

    @Override
    public OrderDetailResponse orderDetail(Long orderId) {
        OrderDetailResponse response = queryFactory
                .select(new QOrderDetailResponse(
                        orders.id,
                        goods.name,
                        goods.fishingStartTime.substring(0,2).concat(":").concat(goods.fishingStartTime.substring(2,4)),
                        goods.totalAmount,
                        orders.orderNumber,
                        orders.orderStatus,
                        orders.createdBy.memberName,
                        orders.createdBy.phoneNumber.areaCode.concat(orders.createdBy.phoneNumber.localNumber),
                        orders.createdDate,
                        orders.payMethod,
                        orders.totalAmount,
                        orders.discountAmount,
                        ship.positions,
                        orderDetails.positions,
                        ship.weight
                ))
                .from(orders).join(orderDetails).on(orderDetails.orders.eq(orders)).join(goods).on(orders.goods.eq(goods)).join(ship).on(goods.ship.eq(ship))
                .where(orders.id.eq(orderId))
                .fetchOne();
        return response;
    }


    private BooleanExpression eqStatus(String status) {
        return status.isEmpty() ? null : orders.orderStatus.eq(OrderStatus.valueOf(status));
    }

    private BooleanExpression eqPayMethod(String method) {
        return method.isEmpty() ? null : orders.payMethod.eq(PayMethod.valueOf(method));
    }

    private BooleanExpression containsKeyword(String type, String keyword, String keys) {
        BooleanExpression expression;
        switch (type) {
            case "username":
                expression = orders.createdBy.memberName.containsIgnoreCase(keyword);
                break;
            case "phone":
                expression = orders.createdBy.phoneNumber.areaCode.concat(orders.createdBy.phoneNumber.localNumber).containsIgnoreCase(keyword.replaceAll("-", ""));
                break;
            case "shipName":
                expression = orders.goods.ship.shipName.containsIgnoreCase(keyword);
                break;
            case "goodsName":
                expression = orders.goods.name.containsIgnoreCase(keyword);
                break;
            default:
                expression = null;
                break;
        }
        return expression;
    }

    private BooleanExpression betweenDate(String startDate, String endDate) {
        if (startDate.equals("") || endDate.equals("")) {
            return null;
        }
        LocalDateTime start = DateUtils.getDateFromString(startDate).atTime(0, 0);
        LocalDateTime end = DateUtils.getDateFromString(endDate).atTime(0, 0);
        return orders.createdDate.between(start, end);
    }

    @Override
    public List<Orders> getOrderByStatus(String date, OrderStatus status) {
        return queryFactory
                .selectFrom(orders)
                .where(orders.fishingDate.eq(date),
                        orders.orderStatus.eq(status))
                .fetch();
    }

    @Override
    public List<Orders> getOrderByStatusForScheduler(String date, String time, OrderStatus status) {
        return queryFactory
                .selectFrom(orders)
                .where(orders.fishingDate.eq(date),
                        orders.orderStatus.eq(status),
                        orders.goods.fishingStartTime.substring(0,2).eq(time))
                .fetch();
    }

    @Override
    public List<OrderDetails> getNextOrders(Integer personnel, Goods good, String fishingDate) {
        return queryFactory
                .select(orderDetails)
                .from(orders).join(orderDetails).on(orders.eq(orderDetails.orders))
                .where(orders.orderStatus.eq(OrderStatus.waitBook),
                        orderDetails.personnel.lt(personnel).or(orderDetails.personnel.eq(personnel)),
                        orders.goods.eq(good),
                        orders.fishingDate.eq(fishingDate))
                .orderBy(orders.createdDate.asc())
                .fetch();
    }

    @Override
    public Integer getPersonnelByFishingDate(Goods good, String fishingDate) {
        return queryFactory
                .select(orderDetails.personnel.sum())
                .from(orders).join(orderDetails).on(orders.eq(orderDetails.orders))
                .where(orders.goods.eq(good),
                        orders.fishingDate.eq(fishingDate))
                .fetchOne();
    }
}
