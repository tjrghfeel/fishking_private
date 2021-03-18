package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideShipRepository extends BaseRepository<RideShip, Long> {

   // List<RideShip> findAll(Specification<RideShip> rideShipSpecification);
    @Query("select r from RideShip r where r.ordersDetail.orders.id = :orderId")
    List<RideShip> findRideByOrder(Long orderId);

}
