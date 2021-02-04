package com.tobe.fishking.v2.repository.fishking.specs;

import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import org.jcodec.common.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class OrderDetailsSpecs {

    public static Specification<OrderDetails> orderDate(final String orderDate) {
        return new Specification<OrderDetails>() {
            public Predicate toPredicate(Root<OrderDetails> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if(StringUtils.isEmpty(orderDate)) return null;

                Join<OrderDetails, Orders> m = root.join("orders", JoinType.INNER);

                return builder.equal(m.get("orderDate"), orderDate);
            }
        };
    }

    public static Specification<OrderDetails> isOrderStatus() {
        return new Specification<OrderDetails>() {
            public Predicate toPredicate(Root<OrderDetails> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                Join<OrderDetails, Orders> m = root.join("orders", JoinType.INNER);
                
                return builder.equal(m.get("orderStatus"), OrderStatus.bookFix);

            }
        };
    }
}