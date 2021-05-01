package com.tobe.fishking.v2.service.fishking;


import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.fishing.OrdersInfoDTO;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.model.fishing.SearchOrdersDTO;
import com.tobe.fishking.v2.model.response.FishingDashboardResponse;
import com.tobe.fishking.v2.model.response.OrderDetailResponse;
import com.tobe.fishking.v2.model.response.OrderListResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateDetailResponse;
import com.tobe.fishking.v2.model.smartfishing.CalculateResponse;
import com.tobe.fishking.v2.repository.fishking.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final ShipRepository shipRepository;
    private final CalculateRepository calculateRepository;
    private final GoodsFishingDateRepository goodsFishingDateRepository;


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
            OrderDetails orderDetails = orderDetailsRepo.findByOrders(orders);
            OrderStatus orderStatus = orders.getOrderStatus();
            Goods goods = orders.getGoods();
            GoodsFishingDate goodsFishingDate = goodsFishingDateRepository
                    .findByGoodsIdAndDateString(goods.getId(), orders.getFishingDate());
            if (orderStatus.equals(OrderStatus.bookRunning)) {
                orders.changeStatus(OrderStatus.bookConfirm);
                ordersRepository.save(orders);
                goodsFishingDate.addReservedNumber(orderDetails.getPersonnel());
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean cancelOrder(Long orderId, Member member) {
        try {
            Orders orders = ordersRepository.getOne(orderId);
            orders.cancelled(member, "cancel"+ LocalDateTime.now().toString());
            ordersRepository.save(orders);
            return true;
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
    public FishingDashboardResponse getStatus(Long memberId) {
        Long countRunning = 0L;
        Long countConfirm = 0L;
        Long countWait = 0L;
        Long countFix = 0L;
        Long countCancel = 0L;
        Long countComplete = 0L;

        List<Tuple> list = ordersRepository.getStatus(memberId);
        for (Tuple tuple : list) {
            switch (tuple.get(0, OrderStatus.class)) {
                case book:
                    break;
                case bookRunning:
                    countRunning = tuple.get(1, Long.class);
                    break;
                case waitBook:
                    countWait = tuple.get(1, Long.class);
                    break;
                case bookFix:
                    countFix = tuple.get(1, Long.class);
                    break;
                case bookCancel:
                    countCancel = tuple.get(1, Long.class);
                    break;
                case fishingComplete:
                    countComplete = tuple.get(1, Long.class);
                    break;
                case bookConfirm:
                    countConfirm = tuple.get(1, Long.class);
                    break;
                default:
            }
        }
        return new FishingDashboardResponse(countRunning, countConfirm, countWait, countFix, countCancel, countComplete);
    }

    @Transactional
    public Map<String, Object> searchCalculation(Long memberId, String shipName, String year, String month, Boolean isCalculate) {
        List<Tuple> ships = shipRepository.getGoodsShips(memberId);
        List<CalculateResponse> calcs = calculateRepository.searchCalculate(memberId, shipName, year, month, isCalculate);
        List<Long> ids = calcs.stream().map(CalculateResponse::getShipId).collect(Collectors.toList());
        for (Tuple tuple : ships) {
            Long id = tuple.get(0, Long.class);
            String name = tuple.get(1, String.class);
            if (!ids.contains(id)) {
                calcs.add(new CalculateResponse(id, name, 0L, 0L));
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("month", month);
        response.put("content", calcs);
        return response;
    }

    @Transactional
    public Map<String, Object> getCalculationThisMonth(Long memberId) {
        List<Tuple> ships = shipRepository.getGoodsShips(memberId);
        LocalDate today = LocalDate.now();
        List<CalculateResponse> calcs = calculateRepository.searchCalculate(memberId, "", String.valueOf(today.getYear()), String.valueOf(today.getMonthValue()), null);
        List<Long> ids = calcs.stream().map(CalculateResponse::getShipId).collect(Collectors.toList());
        for (Tuple tuple : ships) {
            Long id = tuple.get(0, Long.class);
            String name = tuple.get(1, String.class);
            if (!ids.contains(id)) {
                calcs.add(new CalculateResponse(id, name, 0L, 0L));
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("year", String.valueOf(today.getYear()));
        response.put("month", String.valueOf(today.getMonthValue()));
        response.put("content", calcs);
        return response;
    }

    @Transactional
    public Map<String, Object> getCalculateDetail(Long shipId, String year, String month) {
        List<CalculateDetailResponse> calcs = calculateRepository.calculateDetail(shipId, year, month);
        Ship ship = shipRepository.getOne(shipId);
        Map<String, Object> response = new HashMap<>();
        response.put("year", year);
        response.put("month", month);
        response.put("shipId", ship.getId());
        response.put("shipName", ship.getShipName());
        response.put("total", calcs.stream().mapToInt(CalculateDetailResponse::getPayAmount).sum());
        response.put("cancel", calcs.stream().filter(c -> c.getPayAmount() < 0).mapToInt(CalculateDetailResponse::getPayAmount).sum());
        response.put("order", calcs.stream().filter(c -> c.getPayAmount() >= 0).mapToInt(CalculateDetailResponse::getPayAmount).sum());
        response.put("content", calcs);
        return response;
    }

}
