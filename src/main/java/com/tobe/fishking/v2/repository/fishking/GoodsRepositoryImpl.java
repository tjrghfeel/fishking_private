package com.tobe.fishking.v2.repository.fishking;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.model.fishing.GoodsResponse;
import com.tobe.fishking.v2.model.response.GoodsSmallResponse;
import com.tobe.fishking.v2.model.response.QGoodsSmallResponse;
import com.tobe.fishking.v2.model.response.QUpdateGoodsResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.tobe.fishking.v2.entity.common.QLoveTo.loveTo;
import static com.tobe.fishking.v2.entity.fishing.QGoods.goods;
import static com.tobe.fishking.v2.entity.fishing.QOrderDetails.orderDetails;
import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;
import static com.tobe.fishking.v2.entity.fishing.QShip.ship;

@RequiredArgsConstructor
public class GoodsRepositoryImpl implements GoodsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GoodsResponse> getShipGoods(Long ship_id, LocalDate date) {
        NumberPath<Integer> countAlias = Expressions.numberPath(Integer.class, "count");
        QueryResults<GoodsResponse> result = queryFactory
                .select(Projections.constructor(GoodsResponse.class,
                        goods,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(orderDetails.personnel.sum())
                                        .from(orderDetails)
                                        .where(orderDetails.goods.id.eq(goods.id), orderDetails.orders.fishingDate.eq(DateUtils.getDateInFormat(date)))
                                , countAlias)
                ))
                .from(goods)
                .where(goods.ship.id.eq(ship_id), goods.isUse.eq(true), goods.fishingDates.any().fishingDate.eq(date))
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
                        goods.isUse
                ))
                .from(goods)
                .where(goods.ship.id.eq(shipId), goods.name.containsIgnoreCase(keyword), eqStatus(status))
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
}
