package com.tobe.fishking.v2.repository.fishking.specs;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import org.jcodec.common.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class OrderDetailsSpecs {

    /*주문일자*/
    public static Specification<OrderDetails> orderDate(final String orderDate) {
        return new Specification<OrderDetails>() {
            public Predicate toPredicate(Root<OrderDetails> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if(StringUtils.isEmpty(orderDate)) return null;

                Join<OrderDetails, Orders> m = root.join("orders", JoinType.INNER);

                return builder.equal(m.get("orderDate"), orderDate);
            }
        };
    }

    /*상품 (출항)일자 */
    public static Specification<OrderDetails> fishingDate(final String fishingDate) {
        return new Specification<OrderDetails>() {
            public Predicate toPredicate(Root<OrderDetails> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

                if(StringUtils.isEmpty(fishingDate)) return null;

                Join<OrderDetails, Goods> m = root.join("goods", JoinType.INNER);

                return builder.equal(m.get("fishingDate"), fishingDate);
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