package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.utils.QueryDslUtil.getSortedColumn;

@RequiredArgsConstructor
public class RideShipRepositoryImpl implements RideShipRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public void getTodayRiders(Long memberId, String orderBy) {
        OrderSpecifier<?> order;

        switch (orderBy) {
            case "shipName":
                order = getSortedColumn(Order.ASC, ship, "shipName");
                break;
            case "username":
                order = getSortedColumn(Order.ASC, rideShip, "name");
                break;
            default:
                order = getSortedColumn(Order.DESC, rideShip, "createDate");

        }

        String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        queryFactory
                .select()
                .from(rideShip)
                    .join(orderDetails).on(rideShip.ordersDetail.eq(orderDetails))
                    .join(goods).on(orderDetails.goods.eq(goods))
                    .join(ship).on(goods.ship.eq(ship))
                .where(ship.company.member.id.eq(memberId),
                        orderDetails.orders.fishingDate.eq(now))
                .orderBy(order);

    }
}
