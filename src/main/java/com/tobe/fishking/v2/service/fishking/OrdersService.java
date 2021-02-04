package com.tobe.fishking.v2.service.fishking;


import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.model.fishing.OrdersInfoDTO;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.repository.fishking.OrderDetailsRepository;
import com.tobe.fishking.v2.repository.fishking.RideShipRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.tobe.fishking.v2.repository.fishking.specs.OrderDetailsSpecs.isOrderStatus;
import static com.tobe.fishking.v2.repository.fishking.specs.OrderDetailsSpecs.orderDate;
import static com.tobe.fishking.v2.repository.fishking.specs.RideShipSpecs.goodsIdEqu;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class OrdersService {


    private final OrderDetailsRepository orderDetailsRepo;
    private final RideShipRepository rideShipRepo;


    private static ModelMapper modelMapper = new ModelMapper();

    public RiderShipDTO.RiderShipDTOResp findAllByDateAndRider(String orderDate, OrderStatus orderstatus) {
        return orderDetailsRepo.findAllByDateAndRider(orderDate, orderstatus);
    }

    public List<OrdersInfoDTO.ShipByOrdersDTOResp> findAllByOrderDate(String orderDate) {

        //orderStatus  는 완료된것만 조회
        List<OrderDetails> orderDetailsList = orderDetailsRepo.findAll(where(orderDate(orderDate)).and(isOrderStatus()));


        //   List<OrdersInfoDTO.ShipByOrdersDTOResp> ShipByOrdersDTORespList = ModelMapperUtils.mapAll(resultOrderDetails, OrdersInfoDTO.ShipByOrdersDTOResp.class);

        return orderDetailsList.stream().map(OrderDetails -> OrdersInfoDTO.ShipByOrdersDTOResp.of(OrderDetails)).collect(Collectors.toList());
    }

    public List<RiderShipDTO.BoardingListByOrdersDTOResp> findAllByBoardingList(Long goodsId) {

        List<RideShip> resultRideShipPassengerList = rideShipRepo.findAll(where(goodsIdEqu(goodsId)));

        return resultRideShipPassengerList.stream().map(RideShip -> RiderShipDTO.BoardingListByOrdersDTOResp.of(RideShip)).collect(Collectors.toList());

    }


}
