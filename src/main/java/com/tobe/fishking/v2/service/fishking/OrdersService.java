package com.tobe.fishking.v2.service.fishking;


import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.fishing.OrdersInfoDTO;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.OrderDetailResponse;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import com.tobe.fishking.v2.repository.fishking.OrderDetailsRepository;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import com.tobe.fishking.v2.repository.fishking.RideShipRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tobe.fishking.v2.repository.fishking.specs.OrderDetailsSpecs.fishingDate;
import static com.tobe.fishking.v2.repository.fishking.specs.OrderDetailsSpecs.isOrderStatus;
import static com.tobe.fishking.v2.repository.fishking.specs.RideShipSpecs.goodsIdEqu;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final Environment env;

    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepo;
    private final RideShipRepository rideShipRepo;


    private static ModelMapper modelMapper = new ModelMapper();

    public RiderShipDTO.RiderShipDTOResp findAllByDateAndRider(String orderDate, OrderStatus orderstatus) {
        return orderDetailsRepo.findAllByDateAndRider(orderDate, orderstatus);
    }

    public List<OrdersInfoDTO.ShipByOrdersDTOResp> findAllByFishingDate(String fishingDate) {

         fishingDate = "20201015";
        //orderStatus  는 완료된것만 조회
        List<OrderDetails> orderDetailsList = orderDetailsRepo.findAll(where(fishingDate(fishingDate)).and(isOrderStatus()));

        //   List<OrdersInfoDTO.ShipByOrdersDTOResp> ShipByOrdersDTORespList = ModelMapperUtils.mapAll(resultOrderDetails, OrdersInfoDTO.ShipByOrdersDTOResp.class);

        return orderDetailsList.stream().map(OrderDetails -> OrdersInfoDTO.ShipByOrdersDTOResp.of(OrderDetails)).collect(Collectors.toList());
    }

    public List<RiderShipDTO.BoardingListByOrdersDTOResp> findAllByBoardingList(Long goodsId) {

        List<RideShip> resultRideShipPassengerList = rideShipRepo.findAll(where(goodsIdEqu(goodsId)));

        return resultRideShipPassengerList.stream().map(RideShip -> RiderShipDTO.BoardingListByOrdersDTOResp.of(RideShip)).collect(Collectors.toList());

    }

    public void updateOrderStatus() {

    }

    @Transactional
    public Page<OrderListResponse> searchOrders(SearchOrdersDTO dto, Long memberId, Integer page) throws EmptyListException {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdDate").descending());
        Page<OrderListResponse> orderListResponses = ordersRepository.searchOrders(dto, memberId, pageable, env.getProperty("encrypKey.key"));
        if (orderListResponses.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return orderListResponses;
        }
    }

    @Transactional
    public OrderDetailResponse getOrderDetail(Long orderId) {
        OrderDetailResponse response = ordersRepository.orderDetail(orderId);
        List<RideShip> rides = rideShipRepo.findRideByOrder(orderId);
        for (RideShip ride : rides) {
            Map<String, Object> r = new HashMap<>();
            r.put("name", ride.getName());
            r.put("phone", ride.getPhoneNumber());
            r.put("birthday", ride.getBirthday());
            r.put("emergencyPhone", ride.getEmergencyPhone());
            response.getRideList().add(r);
        }
        return response;
    }

    @Transactional
    public boolean confirmOrder(Long orderId) {
        try {
            Orders orders = ordersRepository.getOne(orderId);
            OrderStatus orderStatus = orders.getOrderStatus();
            if (orderStatus.equals(OrderStatus.bookRunning)) {
                orders.changeStatus(OrderStatus.bookConfirm);
                ordersRepository.save(orders);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public List<OrderListResponse> getBookRunning(Long memberId) throws EmptyListException {
        List<OrderListResponse> response = ordersRepository.getBookRunning(memberId);
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public List<OrderListResponse> getBookConfirm(Long memberId) throws EmptyListException {
        List<OrderListResponse> response = ordersRepository.getBookConfirm(memberId);
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public Map<String, Object> getStatus(Long memberId) {
        Map<String, Object> response = new HashMap<>();
        return response;
    }

}
