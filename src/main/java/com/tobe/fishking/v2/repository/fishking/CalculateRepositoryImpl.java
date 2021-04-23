package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.model.smartfishing.CalculateDetailResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;
import com.tobe.fishking.v2.model.smartfishing.QCalculateDetailResponse;
import com.tobe.fishking.v2.model.smartfishing.QCalculateResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tobe.fishking.v2.entity.fishing.QCalculate.calculate;
import static com.tobe.fishking.v2.entity.fishing.QCompany.company;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;
import static com.tobe.fishking.v2.entity.fishing.QOrders.orders;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;

@RequiredArgsConstructor
public class CalculateRepositoryImpl implements CalculateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CalculateResponse> searchCalculate(Long memberId, String shipName, String year, String month, Boolean status) {
        if(month.length() == 1) {
            month = "0" + month;
        }
        List<CalculateResponse> response = queryFactory
                .select(new QCalculateResponse(ship.id.as("id"),
                        ship.shipName.as("name"),
                        new CaseBuilder().when(calculate.isCancel.eq(true)).then(calculate.amount.multiply(-1)).otherwise(calculate.amount).sum().as("total"),
                        new CaseBuilder().when(calculate.isCancel.eq(true)).then(calculate.amount.multiply(-1)).otherwise(0L).sum().as("cancel"))
                )
                .from(calculate).join(ship).on(calculate.ship.eq(ship)).join(company).on(ship.company.eq(company)).join(orders).on(calculate.orders.eq(orders))
                .where(company.member.id.eq(memberId),
                        calculate.year.eq(year),
                        calculate.month.eq(month),
                        byStatus(status),
                        ship.shipName.containsIgnoreCase(shipName))
                .groupBy(ship.id)
                .fetch();
        return response;
    }

    private BooleanExpression byStatus(Boolean status) {
        if (status == null) {
            return null;
        } else {
            return  calculate.isCalculate.eq(status);
        }
    }

    @Override
    public List<CalculateDetailResponse> calculateDetail(Long shipId, String year, String month) {
        if(month.length() == 1) {
            month = "0" + month;
        }
        List<CalculateDetailResponse> response = queryFactory
                .select(new QCalculateDetailResponse(
                        orders.orderDate,
                        orders.createdBy.memberName,
                        orders.goods.name,
                        orderDetails.personnel,
                        new CaseBuilder().when(calculate.isCancel.eq(true)).then(calculate.amount.multiply(-1)).otherwise(calculate.amount).as("payAmount")
                ))
                .from(calculate).join(ship).on(calculate.ship.eq(ship)).join(orders).on(calculate.orders.eq(orders)).join(orderDetails).on(orders.eq(orderDetails.orders))
                .where(ship.id.eq(shipId),
                        calculate.year.eq(year),
                        calculate.month.eq(month))
                .orderBy(orders.createdDate.asc())
                .fetch();
        return response;
    }

//    @Override
//    public Page<CalculateResponseForManage> searchCalculateForManage(Long memberId, String shipName, String year, String month, Boolean status, Pageable pageable) {
//        if(month.length() == 1) {
//            month = "0" + month;
//        }
//        QueryResults<CalculateResponseForManage> response = null;
//        response = queryFactory
//                .select(new QCalculateResponseForManage(ship.id.as("id"),
//                        ship.shipName.as("name"),
//                        new CaseBuilder().when(calculate.isCancel.eq(true)).then(calculate.amount.multiply(-1)).otherwise(calculate.amount).sum().as("total"),
//                        new CaseBuilder().when(calculate.isCancel.eq(true)).then(calculate.amount.multiply(-1)).otherwise(0L).sum().as("cancel"))
//                )
//                .from(calculate).join(ship).on(calculate.ship.eq(ship)).join(company).on(ship.company.eq(company)).join(orders).on(calculate.orders.eq(orders))
//                .where(company.member.id.eq(memberId),
//                        calculate.year.eq(year),
//                        calculate.month.eq(month),
//                        byStatus(status),
//                        ship.shipName.containsIgnoreCase(shipName))
//                .groupBy(ship.id)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//
//        return new PageImpl<>(response.getResults(), pageable, response.getTotal());
////        return response;
//
//    }
//
}
