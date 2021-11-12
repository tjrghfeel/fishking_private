package com.tobe.fishking.v2.service.smartsail;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.addon.CommonAddon;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.HarborCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.fishing.FingerType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.smartsail.*;
import com.tobe.fishking.v2.repository.common.HarborCodeRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.HttpRequestService;
import com.tobe.fishking.v2.service.NaksihaeService;
import com.tobe.fishking.v2.utils.DateUtils;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardingService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RideShipRepository rideShipRepository;
    private final RiderFingerPrintRepository riderFingerPrintRepository;
    private final HttpRequestService httpRequestService;
    private final SailorRepository sailorRepository;
    private final EntryExitReportRepository entryExitReportRepository;
    private final EntryExitAttendRepository entryExitAttendRepository;
    private final GoodsRepository goodsRepository;
    private final HarborCodeRepository harborCodeRepository;

    private final NaksihaeService naksihaeService;

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
                    print.updateCount(member);
                    result = true;
                }
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    @Transactional
    public void addNewRider(Member member, AddRiderDTO dto) {
        Goods goods = goodsRepository.getOne(dto.getGoodsId());
        LocalDate localDate = LocalDate.parse(dto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        EntryExitReport report = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate).get(0);

        OrderDetails orderDetails = orderDetailsRepository.getByGoodsAndDateAndMember(goods, dto.getDate(), member);
        RideShip rider = new RideShip(
                orderDetails,
                dto.getName(),
                dto.getBirthDate().contains("-") ? dto.getBirthDate() : dto.getBirthDate().substring(0,4) + "-" + dto.getBirthDate().substring(4,6) + "-" + dto.getBirthDate().substring(6),
                dto.getPhone().contains("-") ? dto.getPhone() : CommonAddon.addDashToPhoneNum(dto.getPhone()),
                dto.getEmergencyPhone().contains("-") ? dto.getEmergencyPhone() : CommonAddon.addDashToPhoneNum(dto.getEmergencyPhone()),
                dto.getSex(),
                dto.getAddr(),
                member);
        orderDetails.plusPersonnel(member);
        orderDetailsRepository.save(orderDetails);
        rideShipRepository.save(rider);

        entryExitAttendRepository.save(
                EntryExitAttend.builder()
                        .report(report)
                        .name(dto.getName())
                        .birth(dto.getBirthDate().contains("-") ? dto.getBirthDate() : dto.getBirthDate().substring(0,4) + "-" + dto.getBirthDate().substring(4,6) + "-" + dto.getBirthDate().substring(6))
                        .sex(dto.getSex())
                        .type("0")
                        .rideShipId(rider.getId())
                        .idNumber("")
                        .addr(dto.getAddr())
                        .phone(dto.getPhone().contains("-") ? dto.getPhone() : CommonAddon.addDashToPhoneNum(dto.getPhone()))
                        .emerNum(dto.getEmergencyPhone().contains("-") ? dto.getEmergencyPhone() : CommonAddon.addDashToPhoneNum(dto.getEmergencyPhone()))
                        .build()
        );
    }

//    @Transactional
//    public void addRider(Member member, AddRiderDTO dto) {
//        OrderDetails orderDetails = orderDetailsRepository.findByOrders(dto.getOrderId());
//        RideShip rider = new RideShip(
//                orderDetails,
//                dto.getName(),
//                dto.getBirthDate().contains("-") ? dto.getBirthDate() : dto.getBirthDate().substring(0,4) + "-" + dto.getBirthDate().substring(4,6) + "-" + dto.getBirthDate().substring(6),
//                dto.getPhone().contains("-") ? dto.getPhone() : CommonAddon.addDashToPhoneNum(dto.getPhone()),
//                dto.getEmergencyPhone().contains("-") ? dto.getEmergencyPhone() : CommonAddon.addDashToPhoneNum(dto.getEmergencyPhone()),
//                dto.getSex(),
//                dto.getAddr(),
//                member);
//        orderDetails.plusPersonnel(member);
//        orderDetailsRepository.save(orderDetails);
//        rideShipRepository.save(rider);
//    }

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
            r.put("birthday", tuple.get(6, String.class) == null ? "" : tuple.get(6, String.class));
            r.put("sex", tuple.get(7, String.class) == null ? "" :tuple.get(7, String.class).equals("M") ? "남" : "여");
            r.put("addr", tuple.get(8, String.class) == null ? "" : tuple.get(8, String.class));
            r.put("isRide", rideString);
            list.add(r);
        }
        response.put("riders", list);
        response.put("reserveComment", orders.getReserveComment());

        return response;
    }

    @Transactional
    public List<SailorResponse> getSailors(Member member) {
        return sailorRepository.findAllByCreatedBy(member);
    }

    @Transactional
    public void addSailor(Member member, Map<String, Object> body) {
        Goods goods = goodsRepository.getOne(Long.parseLong(body.get("goodsId").toString()));
        LocalDate localDate = LocalDate.parse(body.get("date").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        EntryExitReport report = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate).get(0);
        Integer sNumber = Integer.parseInt(body.get("idNumber").toString().substring(6,7));
        String birth = sNumber>2 ? "20" + body.get("idNumber").toString().substring(0,6) : "19" + body.get("idNumber").toString().substring(0,6);
        String sex = sNumber%2==0 ? "F" : "M";
        if (!birth.contains("-")) {
            birth = birth.substring(0,4) + "-" + birth.substring(4,6) + "-" + birth.substring(6);
        }
        if (Long.parseLong(body.get("id").toString()) == 0) {
            Sailor sailor =  Sailor.builder()
                    .name(body.get("name").toString())
                    .birth(birth)
                    .sex(sex)
                    .idNumber(body.get("idNumber").toString())
                    .addr(body.get("address").toString())
                    .phone(body.get("phone").toString())
                    .emerNum(body.get("emergencyPhone").toString())
                    .member(member)
                    .build();
            sailorRepository.save(sailor);
            entryExitAttendRepository.save(
                    EntryExitAttend.builder()
                            .report(report)
                            .name(body.get("name").toString())
                            .birth(birth)
                            .sex(sex)
                            .type("2")
                            .rideShipId(null)
                            .idNumber(body.get("idNumber").toString())
                            .addr(body.get("address").toString())
                            .phone(body.get("phone").toString())
                            .emerNum(body.get("emergencyPhone").toString())
                            .build()
            );
        } else {
            entryExitAttendRepository.save(
                    EntryExitAttend.builder()
                            .report(report)
                            .name(body.get("name").toString())
                            .birth(birth)
                            .sex(sex)
                            .type("2")
                            .rideShipId(null)
                            .idNumber(body.get("idNumber").toString())
                            .addr(body.get("address").toString())
                            .phone(body.get("phone").toString())
                            .emerNum(body.get("emergencyPhone").toString())
                            .build()
            );
        }
    }

    @Transactional
    public List<ReportRiderResponse> getReportRiders(Long goodsId, String date, Member member) {
        Goods goods = goodsRepository.getOne(goodsId);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<EntryExitReport> reportList = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate);
        List<ReportRiderResponse> response = new ArrayList<>();
        Orders orders = ordersRepository.getOrdersByGoodsAndDateAndMember(goods, date, member);
        if (orders == null) {
            Orders order = Orders.builder()
                    .orderDate(DateUtils.getDateInFormat(LocalDate.now()))
                    .fishingDate(date)
                    .totalAmount(0)
                    .discountAmount(0)
                    .paymentAmount(0)
                    .isPay(false)
                    .payMethod(PayMethod.CARD)
                    .orderStatus(OrderStatus.book)
                    .goods(goods)
                    .reserveComment("현장 추가용")
                    .createdBy(member)
                    .modifiedBy(member)
                    .build();
            ordersRepository.save(order);
            OrderDetails details = OrderDetails.builder()
                    .goods(goods)
                    .orders(order)
                    .personnel(0)
                    .price(0)
                    .totalAmount(0)
                    .positions("")
                    .createdBy(member)
                    .modifiedBy(member)
                    .build();
            orderDetailsRepository.save(details);
        }
        if (reportList.size() > 0) {
            EntryExitReport report = reportList.get(0);
            response = entryExitAttendRepository.getRiders(report);
        } else {
            Ship ship = goods.getShip();
            List<HarborCode> harborCode = harborCodeRepository.findAllByNameAndDong(ship.getHarborName(), ship.getHarborDong());
            String code = harborCode.get(0).getCode();
            LocalDateTime entryTime = null;
            if (goods.getFishingEndDate().equals("익일")) {
                entryTime = LocalDateTime.parse(
                        date + " " + goods.getFishingEndTime(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
                );
                entryTime = entryTime.plusDays(1L);
            } else {
                entryTime = LocalDateTime.parse(
                        date + " " + goods.getFishingEndTime(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
                );

            }
            EntryExitReport report = EntryExitReport.builder()
                    .serial("")
                    .date(localDate)
                    .goods(goods)
                    .entryHarborCode(code)
                    .entryTime(entryTime)
                    .build();
            report.updateStatus("0");
            entryExitReportRepository.save(report);

            // 선장등록
            entryExitAttendRepository.save(
                    EntryExitAttend.builder()
                            .name(ship.getCapName())
                            .birth(ship.getCapBirth())
                            .sex(ship.getCapSex())
                            .phone(ship.getCapPhone())
                            .addr(ship.getCapAddr())
                            .emerNum(ship.getCapEmerNum())
                            .type("1")
                            .idNumber(ship.getCapIdNumber())
                            .rideShipId(null)
                            .report(report)
                            .build()
            );
            List<RideShip> rideShips = rideShipRepository.findByGoods(goods, date);
            rideShips.forEach(r -> {
                entryExitAttendRepository.save(
                        EntryExitAttend.builder()
                                .name(r.getName())
                                .birth(r.getBirthday())
                                .sex(r.getSex())
                                .phone(r.getPhoneNumber().replaceAll("-", ""))
                                .addr(r.getResidenceAddr())
                                .emerNum(r.getEmergencyPhone().replaceAll("-", ""))
                                .type("0")
                                .idNumber("")
                                .rideShipId(r.getId())
                                .report(report)
                                .build()
                );
            });
            response = entryExitAttendRepository.getRiders(report);
        }
        return response;
    }

    @Transactional
    public EntryExitReport getReport(Long goodsId, String date) {
        Goods goods = goodsRepository.getOne(goodsId);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        EntryExitReport report = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate).get(0);
        return report;
    }

    @Transactional
    public void sendReport(Long goodsId, String date) throws UnsupportedEncodingException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            Goods goods = goodsRepository.getOne(goodsId);
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            EntryExitReport report = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate).get(0);
            List<EntryExitAttend> riders = entryExitAttendRepository.getEntryExitAttendsByReport(report);
            String token = naksihaeService.getToken().get("token").toString();
            String serial = naksihaeService.reportRegistration(goods, riders, token);
            report.updateSerial(serial);
            report.updateStatus("1");
            entryExitReportRepository.save(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public String updateReport(Long goodsId, String date, String status) throws UnsupportedEncodingException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        try {
            Goods goods = goodsRepository.getOne(goodsId);
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            EntryExitReport report = entryExitReportRepository.getReportByGoodsAndDate(goods, localDate).get(0);
            String token = naksihaeService.getToken().get("token").toString();
            String str = naksihaeService.checkReportStatus(
                    report.getSerial(),
                    goods,
                    token
            );
            if (!str.equals("2")) {
                return "신고가 승인되지 않았습니다. 잠시 후 다시 시도 바랍니다.";
            }
            boolean success = naksihaeService.updateReportStatus(
                    report.getSerial(),
                    goods,
                    status,
                    token
            );
            if (success) {
                String statusCode = "";
                switch (status) {
                    case "출항":
                        statusCode = "5";
                        break;
                    case "입항":
                        statusCode = "3";
                        break;
                    case "취소":
                        statusCode = "4";
                        break;
                }
                report.updateStatus(statusCode);
                entryExitReportRepository.save(report);
            }
            return "변경되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "잠시 후 다시 시도 바랍니다.";
        }

    }
}