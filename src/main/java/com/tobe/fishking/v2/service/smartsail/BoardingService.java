package com.tobe.fishking.v2.service.smartsail;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.addon.CommonAddon;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.entity.fishing.RideShip;
import com.tobe.fishking.v2.entity.fishing.RiderFingerPrint;
import com.tobe.fishking.v2.enums.fishing.FingerType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.smartsail.AddRiderDTO;
import com.tobe.fishking.v2.model.smartsail.RiderGoodsListResponse;
import com.tobe.fishking.v2.model.smartsail.RiderSearchDTO;
import com.tobe.fishking.v2.model.smartsail.TodayBoardingResponse;
import com.tobe.fishking.v2.repository.fishking.OrderDetailsRepository;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import com.tobe.fishking.v2.repository.fishking.RideShipRepository;
import com.tobe.fishking.v2.repository.fishking.RiderFingerPrintRepository;
import com.tobe.fishking.v2.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tobe.fishking.v2.entity.fishing.QRideShip.rideShip;

@Service
@RequiredArgsConstructor
public class BoardingService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RideShipRepository rideShipRepository;
    private final RiderFingerPrintRepository riderFingerPrintRepository;
    private final HttpRequestService httpRequestService;

    @Transactional
    public List<TodayBoardingResponse> getTodayBoarding(Member member, String orderBy) {
        List<TodayBoardingResponse> responses = rideShipRepository.getTodayRiders(member.getId(), orderBy, false);
        for (TodayBoardingResponse r : responses) {
            RiderFingerPrint print = rideShipRepository.getFingerPrint(r.getUsername(), r.getPhone());
            if (print == null) {
                r.setFingerType(null);
                r.setVisitCount(null);
                r.setFingerTypeNum(1);
            } else {
                r.setFingerType(print.getFinger().getValue());
                r.setVisitCount(print.getCount());
                r.setFingerTypeNum(print.getFinger().ordinal() + 1);
            }
        }
        return responses;
    }

    @Transactional
    public List<TodayBoardingResponse> getTodayBoardingComplete(Member member, String orderBy) {
        List<TodayBoardingResponse> responses = rideShipRepository.getTodayRiders(member.getId(), orderBy, true);
        for (TodayBoardingResponse r : responses) {
            RiderFingerPrint print = rideShipRepository.getFingerPrint(r.getUsername(), r.getPhone());
            if (print == null) {
                r.setFingerType(null);
                r.setVisitCount(null);
            } else {
                r.setFingerType(print.getFinger().getValue());
                r.setVisitCount(print.getCount());
            }
        }
        return responses;
    }

    @Transactional
    public Map<String, Object> getDashboard(Member member) {
        return rideShipRepository.dashboard(member.getId());
    }

    @Transactional
    public void updateFingerprint(Member member, Map<String, Object> body) {
        RideShip rider = rideShipRepository.getOne(Long.parseLong(body.get("riderId").toString()));
        String username = body.get("username").toString();
        String phone = body.get("phone").toString();
        String fingerprint = body.get("fingerprint").toString();
        Integer fingerTypeNum = (Integer) body.get("fingerTypeNum");
        FingerType fingerType = FingerType.values()[fingerTypeNum - 1];

        RiderFingerPrint print = rideShipRepository.getFingerPrint(username, phone);
        if (print == null) {
            riderFingerPrintRepository.save(
                    RiderFingerPrint.builder()
                            .finger(fingerType)
                            .fingerprint(fingerprint)
                            .phone(phone)
                            .name(username)
                            .member(member)
                            .build()
            );
        } else {
            print.updateFingerprint(fingerType, fingerprint, member);
            riderFingerPrintRepository.save(print);
        }

        rider.setRide();
        rideShipRepository.save(rider);
        Orders orders = rider.getOrdersDetail().getOrders();
        orders.changeStatus(OrderStatus.fishingComplete);
        ordersRepository.save(orders);
    }

    @Transactional
    public boolean checkFingerprint(Member member, Map<String, Object> body) {
        RideShip rider = rideShipRepository.getOne(Long.parseLong(body.get("riderId").toString()));
        Orders orders = rider.getOrdersDetail().getOrders();

        String username = body.get("username").toString();
        String phone = body.get("phone").toString();
        String fingerprint = body.get("fingerprint").toString();
        Integer fingerTypeNum = (Integer) body.get("fingerTypeNum");
        FingerType fingerType = FingerType.values()[fingerTypeNum - 1];

        RiderFingerPrint print = rideShipRepository.getFingerPrint(username, phone);
        boolean result = false;
        if (print == null) {
            riderFingerPrintRepository.save(
                    RiderFingerPrint.builder()
                            .finger(fingerType)
                            .fingerprint(fingerprint)
                            .phone(phone)
                            .name(username)
                            .member(member)
                            .build()
            );
            orders.changeStatus(OrderStatus.fishingComplete);
            ordersRepository.save(orders);
            rider.setRide();
            rideShipRepository.save(rider);
            result = true;
        } else {
            try {
                Map<String, Object> data = httpRequestService.checkFingerPrint(fingerprint, print.getFingerprint());
                if (data.get("match").toString().equals("1")) {
                    orders.changeStatus(OrderStatus.fishingComplete);
                    ordersRepository.save(orders);
                    rider.setRide();
                    rideShipRepository.save(rider);
                    result = true;
                }
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    @Transactional
    public void addRider(Member member, AddRiderDTO dto) {
        OrderDetails orderDetails = orderDetailsRepository.findByOrders(dto.getOrderId());
        RideShip rider = new RideShip(
                orderDetails,
                dto.getName(),
                dto.getBirthDate().contains("-") ? dto.getBirthDate() : dto.getBirthDate().substring(0,4) + "-" + dto.getBirthDate().substring(4,6) + "-" + dto.getBirthDate().substring(6),
                dto.getPhone().contains("-") ? dto.getPhone() : CommonAddon.addDashToPhoneNum(dto.getPhone()),
                dto.getEmergencyPhone().contains("-") ? dto.getEmergencyPhone() : CommonAddon.addDashToPhoneNum(dto.getEmergencyPhone()),
                member);
        orderDetails.plusPersonnel(member);
        orderDetailsRepository.save(orderDetails);
        rideShipRepository.save(rider);
    }

    @Transactional
    public void delRider(Member member, Long riderId) {
        RideShip rider = rideShipRepository.getOne(riderId);
        OrderDetails orderDetails = rider.getOrdersDetail();
        orderDetails.minusPersonnel(member);
        orderDetailsRepository.save(orderDetails);
        rideShipRepository.delete(rider);
    }

    @Transactional
    public Page<RiderGoodsListResponse> searchRider(Member member, RiderSearchDTO dto, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return rideShipRepository.searchRiders(member.getId(), dto, pageable);
    }

    @Transactional
    public Map<String, Object> detailRiders(Long orderId) throws ResourceNotFoundException {
        OrderDetails orderDetails = orderDetailsRepository.findByOrders(orderId);
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("orders not found for this id :: "+orderId));
        List<Tuple> riders = rideShipRepository.getDetailRiders(orderId);

        Map<String, Object> response = new HashMap<>();
        LocalDate date = LocalDate.parse(orderDetails.getOrders().getFishingDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        response.put("orderId", orderId);
        response.put("shipName", orderDetails.getGoods().getShip().getShipName());
        response.put("shipProfileImage", "/resource" + orderDetails.getGoods().getShip().getProfileImage());
        response.put("personnel", orderDetails.getPersonnel());
        response.put("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)")));
        List<Map<String, Object>> list = new ArrayList<>();
        for (Tuple tuple : riders) {
            Map<String, Object> r = new HashMap<>();
            Boolean ride = tuple.get(4, Boolean.class);
            LocalDate now = LocalDate.now();
            String rideString = ride ? "확인완료" : now.isAfter(date) ? "확인실패" : "확인전" ;

            r.put("riderId", tuple.get(0, Long.class));
            r.put("name", tuple.get(1, String.class));
            r.put("phone", tuple.get(2, String.class));
            r.put("emergencyPhone", tuple.get(3, String.class));
            r.put("isRide", rideString);
            list.add(r);
        }
        response.put("riders", list);
        response.put("reserveComment", orders.getReserveComment());

        return response;
    }
}
