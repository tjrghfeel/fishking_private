package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.repository.fishking.OrderDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepo;

    public List<OrderDetails> findAllByOrderDate(String orderDate) {

        return orderDetailsRepo.findAll(
                new Specification<OrderDetails>() {
                    @Override
                    public Predicate toPredicate(Root<OrderDetails> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Subquery<OrderDetails> sq = query.subquery(OrderDetails.class);
                        Root<Orders> oprders = sq.from(Orders.class);
                        Join<Orders, OrderDetails> sqEmp = oprders.join("orderdatails");
                        sq.select(sqEmp).where(cb.equal(oprders.get("orderdate"),
                                cb.parameter(String.class, orderDate)));
                        return cb.in(root).value(sq);
                    }
                }
        );
    }
}
