package com.tobe.fishking.v2.repository.fishking.specs;

import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import org.jcodec.common.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class RideShipSpecs {

    public static Specification<RideShip> goodsIdEqu(final Long goodsId) {
        return new Specification<RideShip>() {
            public Predicate toPredicate(Root<RideShip> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if(goodsId <= 0.0) return null;
                return builder.equal(root.get("ordersDetail").get("goods").get("id"), goodsId);
            }
        };
    }

}