package com.tobe.fishking.v2.service.fishking;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs;
import com.tobe.fishking.v2.utils.DateUtils;
import com.tobe.fishking.v2.utils.SpecBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs.fishingType;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final UploadService uploadService;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final ShipRepository shipRepo;
    private final PopularRepository popularRepo;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final EventRepository eventRepository;
    private final ObserverCodeRepository observerCodeRepository;
    private final LoveToRepository loveToRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RideShipRepository rideShipRepository;
    private final GoodsRepository goodsRepository;
    private final GoodsFishingDateRepository goodsFishingDateRepository;


    /*
     *//*업체요청시 등록한 증빙서류File에 대한 downloadUrl을 받아오기위해 FileEntity들을 가져와줌. *//*
    FileEntity ship = fileRepository.findById(ship.getId())
            .orElseThrow(()->new ResourceNotFoundException("files not found for this id ::"+company.getBizNoFileId()));

    */

    //검색 --  name으로 검색
    public Page<ShipDTO.ShipDTOResp> getShipList(Pageable pageable,
                                                 @RequestParam(required = false) Map<String, Object> searchRequest,   ///total를 제외한 모든 것 조회
                                                 Integer totalElement) {

        Map<ShipSpecs.SearchKey, Object> searchKeys = new HashMap<>();

        for (String key : searchRequest.keySet()) {
            searchKeys.put(ShipSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        Page<Ship> ships = searchKeys.isEmpty()
                ? shipRepo.findAll(pageable, totalElement)
                : shipRepo.findAll(ShipSpecs.searchWith(searchKeys), pageable, totalElement);

        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String) searchRequest.get(key), memberRepo.getOne((long) 5)));
        }

        // UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        //file 처리
        return ships.map(ShipDTO.ShipDTOResp::of);
    }

    /*글쓰기시, ship선택버튼에 있는 ship검색 메소드 */
    @Transactional
    public Page<ShipListForWriteFishingDiary> searchShipForWriteFishingDiary(String keyword,/* String sortBy,*/ int page) {

//        if(sortBy.equals("name")){
        Pageable pageable = PageRequest.of(page, 10/*, JpaSort.unsafe(Sort.Direction.DESC,"("+sortBy+")")*/);
        return shipRepo.findBySearchKeyword(keyword, pageable);
        /*}
        //!!!!!정렬기준이 거리순일 경우, gps api를 이용해 처리해야할듯?
        else{
            Pageable pageable = PageRequest.of(page, 10, JpaSort.unsafe(Sort.Direction.DESC,"("+sortBy+")"));
            return shipRepo.findBySearchKeyword(keyword,pageable);
        }*/
    }


    /*지도에 선상 위치 및 정보를 위한 Method*/
    public List<ShipDTO.ShipDTOResp> getShipListsForMap(FilePublish filePublish) {

        FishingType fishingType = filePublish.name().equals("ship") ? FishingType.ship : FishingType.seaRocks;

        //List<Ship> shipEntityList = shipRepo.findAllShipAndLocation(fishingType);

        //List<Ship> shipEntityList = shipRepo.findAllShipAndLocationByFishingType(fishingType);1
        List<Ship> shipEntityList = shipRepo.findAll(where(fishingType(fishingType)));

        List<ShipDTO.ShipDTOResp> shipDTORespList = shipEntityList.stream().map(ShipDTO.ShipDTOResp::of).collect(Collectors.toList());  //O

        //대표이미지
        for (int i = 0; i < shipDTORespList.size(); i++) {

            ShipDTO.ShipDTOResp entity = (ShipDTO.ShipDTOResp) shipDTORespList.get(i);

            FileEntity shipFile = fileRepo.findTop1ByPidAndFilePublishAndIsRepresent(entity.getId(), FilePublish.ship, true);

            if (shipFile != null) entity.setShipImageFileUrl(shipFile.getDownloadThumbnailUrl());
            else entity.setShipImageFileUrl("https://");

            // entity.setFishSpeciesCount(entity.getFishSpecies() == null? 0:entity.getFishSpecies().size());
            shipDTORespList.set(i, entity);

        }

        return shipDTORespList;
        // return new ArrayList<ShipDTO.ShipDTOResp>();
    }

    /* 선상, 갯바위 리스트 */
    @Transactional
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO, int page) {
        Pageable pageable;
        if (shipSearchDTO.getOrderBy().equals("")) {
            pageable = PageRequest.of(page, shipSearchDTO.getSize(), Sort.by(shipSearchDTO.getOrderBy()));
        } else {
            pageable = PageRequest.of(page, shipSearchDTO.getSize(), Sort.by(shipSearchDTO.getOrderBy()));
        }
        return shipRepo.searchAll(shipSearchDTO, pageable);
    }

    /* 선상, 갯바위 배 정보 */
    @Transactional
    public ShipResponse getShipDetail(Long ship_id, String sessionToken) {
        ShipResponse response = shipRepo.getDetail(ship_id);
        List<FishingDiary> diaries = fishingDiaryRepository.getDiaryByShipId(response.getId());
        List<FishingDiary> blogs = fishingDiaryRepository.getBlogByShipId(response.getId());
        response.setFishingDiary(diaries.stream().map(FishingDiaryDTO.FishingDiaryDTOResp::of).collect(Collectors.toList()).subList(0, 3));
        response.setFishingDiaryCount(diaries.size());
        response.setFishingDiary(blogs.stream().map(FishingDiaryDTO.FishingDiaryDTOResp::of).collect(Collectors.toList()).subList(0, 3));
        response.setFishingBlogCount(blogs.size());
        response.setEvents(eventRepository.getEventTitleByShip(ship_id));

        if (!sessionToken.equals("")) {
            Optional<Member> member = memberRepo.findBySessionToken(sessionToken);
            member.ifPresent(value -> response.setLiked(loveToRepository.findByLinkIdAndMember(response.getId(), value) > 0));
        }
        return response;
    }

    /* 선상, 갯바위 배 정보 */
    @Transactional
    public List<GoodsResponse> getShipGoods(Long ship_id) {
        List<GoodsResponse> response = goodsRepository.getShipGoods(ship_id);
        return response;
    }

    @Transactional
    public Map<String, Object> getGoodsDatePositions(Long goods_id, String date) {
        Goods goods = goodsRepository.getOne(goods_id);
        List<OrderDetails> orders = orderDetailsRepository.getByGoodsAndDate(goods, date);
        List<String> availablePositions = new ArrayList<>();
        List<String> usedPositions = new ArrayList<>();
        availablePositions = Arrays.asList(goods.getShip().getPositions().split(",").clone());
        for (OrderDetails details : orders) {
            usedPositions.addAll(Arrays.asList(details.getPositions().split(",").clone()));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("used", usedPositions);
        result.put("total", availablePositions);
        return result;
    }

    @Transactional
    public GoodsResponse getGoodsDetail(Long goods_id) {
        Goods goods = goodsRepository.getOne(goods_id);
        return GoodsResponse.builder()
                .goods(goods)
                .build();
    }

    /* 선상 예약 */
    @Transactional
    public OrderResponse reserve(ReserveDTO reserveDTO, Long member_id, String[] names, String[] phones, String[] birthdates) {
        Member member;
        member = memberRepo.getOne(Objects.requireNonNullElse(member_id, 22L));
        Goods goods = goodsRepository.getOne(reserveDTO.getGoodsId());
        Orders order = Orders.builder()
                .orderDate(DateUtils.getDateInFormat(LocalDate.now()))
                .fishingDate(reserveDTO.getDate())
                .totalAmount(reserveDTO.getTotalPrice().intValue())
                .discountAmount(reserveDTO.getDiscountPrice().intValue())
                .paymentAmount(reserveDTO.getPaymentPrice().intValue())
                .isPay(false)
                .orderStatus(OrderStatus.receipt)
                .goods(goods)
                .createdBy(member)
                .modifiedBy(member)
                .build();
        ordersRepository.save(order);

        String orderNumber = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + order.getId().toString();
        order.setOrderNumber(orderNumber, member);
        ordersRepository.save(order);

        OrderDetails details = OrderDetails.builder()
                .goods(goods)
                .orders(order)
                .personnel(reserveDTO.getPersonCount())
                .price(reserveDTO.getTotalPrice().intValue() / reserveDTO.getPersonCount())
                .totalAmount(reserveDTO.getTotalPrice().intValue())
                .positions(reserveDTO.getPositionsList().stream().map(Object::toString).collect(Collectors.joining(",")))
                .createdBy(member)
                .modifiedBy(member)
                .build();
        orderDetailsRepository.save(details);

        for (int idx = 0 ; idx < names.length; idx++) {
            RideShip rideShip = RideShip.builder()
                    .ordersDetail(details)
                    .birthday(birthdates[idx])
                    .name(names[idx])
                    .phoneNumber(phones[idx])
                    .createdBy(member)
                    .modifiedBy(member)
                    .build();
            rideShipRepository.save(rideShip);
        }

        GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(reserveDTO.getGoodsId(), reserveDTO.getDate());
        goodsFishingDateRepository.save(goodsFishingDate);

        return new OrderResponse(order.getId(),
                orderNumber,
                details.getGoods().getName(),
                reserveDTO.getPaymentPrice().intValue(),
                member.getMemberName(),
                member.getEmail(),
                member.getPhoneNumber().getFullNumber(),
                reserveDTO.getPayMethod());
    }

//    public List<String> getShipPositions(Long goods_id) {
//        Goods goods = goodsRepository.getOne(goods_id);
//        Ship ship = goods.getShip();
//        List<String> positions = Arrays.stream(ship.getPositions().split(",")).collect(Collectors.toList());
//    }

//    public void calcDistance() {
//        List<Ship> ships = shipRepo.findAll();
//        for (Ship ship : ships) {
//            List<ObserverCode> codes = observerCodeRepository.findAll();
//            ObserverCode code = codes.stream()
//                    .sorted(Comparator.comparing(e -> e.distanceFrom(ship.getLocation())))
//                    .collect(Collectors.toList())
//                    .get(0);
//            ship.setObserverCode(code.getCode());
//            shipRepo.save(ship);
//        }
//    }


    public Long findAllShipCount() {
        return shipRepo.findAllByIsActive();
    }

}
