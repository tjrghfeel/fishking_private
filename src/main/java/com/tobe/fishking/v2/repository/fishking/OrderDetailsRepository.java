package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends BaseRepository<OrderDetails, Long> {

    OrderDetails findByOrders(Orders orders);

    @Query("select o from OrderDetails o where o.orders.id = :orderId")
    OrderDetails findByOrders(Long orderId);


    @Query(value = "SELECT sum(s.personnel), sum(s.ridePersonnel)" +
              "FROM OrderDetails s  " +
               " where s.orders.orderDate = :orderDate  " +
               "   and s.orders.orderStatus = :orderStatus" +
               "   group by  s.orders.orderDate , s.orders.orderStatus" )
    RiderShipDTO.RiderShipDTOResp findAllByDateAndRider(@Param("orderDate") String orderdate, @Param("orderStatus") OrderStatus orderStatus);


    // List<OrderDetails> findAll(Specification<OrderDetails> orderDetailsSpecification);
/*
    @Query(value = "SELECT s " +
            "FROM OrderDetails s  " +
            " where s.orders.orderDate = :orderDate  "  )
    List<OrderDetails> findAllByOrderStatusAndOrderDate(@Param("orderDate") String orderDate);
*/
    @Query("select o from OrderDetails o where o.goods = :goods and o.orders.fishingDate = :date and o.orders.isPay = true and o.orders.orderStatus <> 4 order by o.createdDate asc")
    List<OrderDetails> getByGoodsAndDate(Goods goods, String date);

}
