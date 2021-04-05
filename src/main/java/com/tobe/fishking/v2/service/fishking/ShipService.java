package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.board.FishingDiarySmallResponse;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.model.common.ReviewResponse;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs;
import com.tobe.fishking.v2.service.HttpRequestService;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs.fishingType;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final Environment env;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final ShipRepository shipRepo;
    private final PopularRepository popularRepo;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final EventRepository eventRepository;
    private final ObserverCodeRepository observerCodeRepository;
    private final TakeRepository takeRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final RideShipRepository rideShipRepository;
    private final GoodsRepository goodsRepository;
    private final FileRepository fileRepository;
    private final ReviewRepository reviewRepository;
    private final HttpRequestService httpRequestService;
    private final RealTimeVideoRepository realTimeVideoRepository;
    private final PlacesRepository placesRepository;
    private final PlacePointRepository placePointRepository;
    private final CouponRepository couponRepository;
    private final CouponMemberRepository couponMemberRepository;
    private final CompanyRepository companyRepository;


    /*
     *//*업체요청시 등록한 증빙서류 File 에 대한 downloadUrl 을 받아오기위해 FileEntity 들을 가져와줌. *//*
    FileEntity ship = fileRepository.findById(ship.getId())
            .orElseThrow(()->new ResourceNotFoundException("files not found for this id ::"+company.getBizNoFileId()));

    */

    //검색 --  name 으로 검색
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
            pageable = PageRequest.of(page, 10, Sort.by(shipSearchDTO.getOrderBy()));
        } else {
            pageable = PageRequest.of(page, 10, Sort.by(shipSearchDTO.getOrderBy()));
        }
        return shipRepo.searchAll(shipSearchDTO, pageable);
    }

    @Transactional
    public List<ShipListResponse> getShipsForMap(ShipSearchDTO  shipSearchDTO) {
        return shipRepo.searchAllForMap(shipSearchDTO);
    }

    /* 선상, 갯바위 배 정보 */
    @Transactional
    public ShipResponse getShipDetail(Long ship_id, String sessionToken) throws UnsupportedEncodingException {
        ShipResponse response = shipRepo.getDetail(ship_id);
        List<FishingDiary> diaries = fishingDiaryRepository.getDiaryByShipId(response.getId());
        List<FishingDiary> blogs = fishingDiaryRepository.getBlogByShipId(response.getId());
        List<FishingDiarySmallResponse> diaryResponse = new ArrayList<>();
        List<FishingDiarySmallResponse> blogResponse = new ArrayList<>();

        for (FishingDiary diary : diaries) {
            diaryResponse.add(getDiarySmallResponse(diary));
        }

        for (FishingDiary blog : blogs) {
            blogResponse.add(getDiarySmallResponse(blog));
        }

        if (diaryResponse.size() > 3) {
            response.setFishingDiary(diaryResponse.subList(0, 3));
        } else {
            response.setFishingDiary(diaryResponse);
        }
        response.setFishingDiaryCount(diaries.size());

        if (blogResponse.size() > 3) {
            response.setFishingBlog(blogResponse.subList(0, 3));
        } else {
            response.setFishingBlog(blogResponse);
        }
        response.setFishingBlogCount(blogs.size());
        response.setEventsList(eventRepository.getEventByShip(ship_id));

        response.setLiked(false);
        if (!sessionToken.equals("")) {
            Optional<Member> member = memberRepo.findBySessionToken(sessionToken);
            member.ifPresent(value -> response.setLiked(takeRepository.findByLinkIdAndMemberAndType(response.getId(), value, TakeType.ship) > 0));
        }

        if (response.getFishingType().equals("seaRocks")) {
            List<Map<String, Object>> rockData = new ArrayList<>();
            List<Places> places = placesRepository.getPlacesByShipId(response.getId());
            for (Places place : places) {
                Map<String, Object> p = new HashMap<>();
                p.put("name", place.getPlaceName());
                p.put("averageDepth", place.getAverageDepth());
                p.put("floorMaterial", place.getFloorMaterial());
                p.put("tideTime", place.getTideTime());
                p.put("introduce", place.getIntroduce());
                p.put("address", place.getAddress());
                p.put("latitude", place.getLocation().getLatitude());
                p.put("longitude", place.getLocation().getLatitude());
                rockData.add(p);
            }
            response.setRockData(rockData);
        }

        List<RealTimeVideo> videos = realTimeVideoRepository.getRealTimeVideoByShipsId(ship_id);
        if (videos.size() > 0) {
            RealTimeVideo video = videos.get(0);
            if (video.getType().equals("toast")) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime expTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                            TimeZone.getDefault().toZoneId()
                    );
//                LocalDateTime expTime = LocalDateTime.of(2100, 12, 31, 12, 12);
                    String token = "";
                    if (now.isAfter(expTime)) {
//                        Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                        Company company = companyRepository.getCompanyByShip(response.getId());
                        Map<String, Object> tokenData = httpRequestService.getToken(company.getNhnId());
                        token = ((String) tokenData.get("token")).replaceAll("\"", "");
                        String expireTime = (String) tokenData.get("expireTime");
                        realTimeVideoRepository.updateToken(token, expireTime, video.getToken());
                    } else {
                        token = video.getToken();
                    }
                    if (!token.equals("")) {
                        List<Map<String, Object>> cameras = httpRequestService.getCameraList(token);
                        for (Map<String, Object> camera : cameras) {
                            String serial = camera.get("serialNo").toString();
                            if (serial.equals(video.getSerial())) {
                                String type = camera.get("recordType").toString();
                                String streamStatus = camera.get("streamStatus").toString();
                                String controlStatus = camera.get("controlStatus").toString();
                                String liveUrl = "";
                                if (type.equals("24h")) {
                                    if (streamStatus.equals("on")) {
                                        liveUrl = httpRequestService.getPlayUrl(token, serial);
                                    }
                                } else {
                                    if (controlStatus.equals("on")) {
                                        liveUrl = httpRequestService.getPlayUrl(token, serial);
                                    }
                                }
                                response.setLiveVideo(liveUrl);
                            }
                        }
                    }
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            } else {
                Company company = companyRepository.getCompanyByShip(ship_id);
                String token = null;
                try {
                    if (sessionToken.equals("")) {
                        sessionToken = LocalTime.now().toString();
                    }
                    token = httpRequestService.loginADT(company.getAdtId(), company.getAdtPw(), sessionToken);
                    String videoUrl = httpRequestService.getADTCameraLive(video.getSerial(), token);
                    if (videoUrl != null) {
                        response.setLiveVideo(videoUrl);
                    }
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /* 선상, 갯바위 배 정보 */
    @Transactional
    public List<GoodsResponse> getShipGoods(Long ship_id, String date) {
        return goodsRepository.getShipGoods(ship_id, DateUtils.getDateFromString(date));
    }

    @Transactional
    public Map<String, Object> getGoodsDatePositions(Long goods_id, String date) {
        Goods goods = goodsRepository.getOne(goods_id);
        if (goods.getShip().getFishingType().getKey().equals("seaRocks")) {
            return getGoodsDatePositionsSeaRock(goods_id, date);
        } else {
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
            result.put("type", goods.getShip().getWeight());
            result.put("rockData", null);
            return result;
        }
    }

    @Transactional
    public Map<String, Object> getGoodsDatePositionsSeaRock(Long goods_id, String date) {
        Map<String, Object> result = new HashMap<>();
        Goods goods = goodsRepository.getOne(goods_id);
        List<OrderDetails> orders = orderDetailsRepository.getByGoodsAndDate(goods, date);
        List<String> usedPositions = new ArrayList<>();
        List<Places> places = placesRepository.getPlacesByShip(goods.getShip());

        List<Map<String, Object>> rockData = new ArrayList<>();
        for (Places place : places) {
            Map<String, Object> placeData = new HashMap<>();
            placeData.put("name", place.getPlaceName());
            placeData.put("address", place.getAddress());
            placeData.put("latitude", place.getLocation().getLatitude());
            placeData.put("longitude", place.getLocation().getLatitude());
            List<PlacePoint> points = placePointRepository.getPlacePointByPlace(place);
            List<Map<String, Object>> pointList = new ArrayList<>();
            for (PlacePoint point : points) {
                Map<String, Object> pointData = new HashMap<>();
                pointData.put("latitude", point.getLocation().getLatitude());
                pointData.put("longitude", point.getLocation().getLongitude());
                pointData.put("id", point.getId());
                pointList.add(pointData);
            }
            placeData.put("points", pointList);
            rockData.add(placeData);
        }

        for (OrderDetails details : orders) {
            usedPositions.addAll(Arrays.asList(details.getPositions().split(",").clone()));
        }
        result.put("used", usedPositions);
        result.put("rockData", rockData);
        result.put("total", null);
        result.put("type", null);
        return result;
    }

    @Transactional
    public GoodsResponse getGoodsDetail(Long goods_id) {
        Goods goods = goodsRepository.getOne(goods_id);
        return GoodsResponse.builder()
                .goods(goods)
                .rideMember(0)
                .build();
    }

    /* 선상 예약 */
    @Transactional
    public OrderResponse reserve(ReserveDTO reserveDTO, String token, String[] names, String[] phones, String[] emergencyPhones, String[] birthdates) {
        Member member = null;
//        member = memberRepo.getOne(Objects.requireNonNullElse(member_id, 22L));
        Optional<Member> memberOpt = memberRepo.findBySessionToken(token);
        member = memberOpt.orElseGet(() -> memberRepo.getOne(22L));

        Goods goods = goodsRepository.getOne(reserveDTO.getGoodsId());

//        List<OrderDetails> orders = orderDetailsRepository.getByGoodsAndDate(goods, reserveDTO.getDate());
//        List<String> usedPositions = new ArrayList<>();
//        for (OrderDetails details : orders) {
//            usedPositions.addAll(Arrays.asList(details.getPositions().split(",").clone()));
//        }
        PayMethod payMethod;

        switch (reserveDTO.getPayMethod()) {
            case "1000000000":
                payMethod = PayMethod.CARD;
                break;
            case "0100000000":
                payMethod = PayMethod.VIRTUAL_ACCOUNT;
                break;
            case "0010000000":
                payMethod = PayMethod.ACCOUNT;
                break;
            default:
                payMethod = PayMethod.PHONE;
                break;
        }

        Orders order = Orders.builder()
                .orderDate(DateUtils.getDateInFormat(LocalDate.now()))
                .fishingDate(reserveDTO.getDate())
                .totalAmount(reserveDTO.getTotalPrice().intValue())
                .discountAmount(reserveDTO.getDiscountPrice().intValue())
                .paymentAmount(reserveDTO.getPaymentPrice().intValue())
                .isPay(false)
                .payMethod(payMethod)
                .orderStatus(OrderStatus.book)
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
                .positions(reserveDTO.getPositions().stream().map(Object::toString).collect(Collectors.joining(",")))
                .createdBy(member)
                .modifiedBy(member)
                .build();
        orderDetailsRepository.save(details);

        for (int idx = 0 ; idx < names.length; idx++) {
            String birthdate;
            String phone;
            String emergencyPhone;
            if (birthdates[idx].contains("-")) {
                birthdate = birthdates[idx];
            } else {
                birthdate = birthdates[idx].substring(0,4) + "-" + birthdates[idx].substring(4,6) + "-" + birthdates[idx].substring(6);
            }
            if (phones[idx].contains("-")) {
                phone = phones[idx];
            } else {
                if (phones[idx].length() == 11) {
                    phone = phones[idx].substring(0,3) + "-" + phones[idx].substring(3,6) + "-" + phones[idx].substring(6);
                } else {
                    phone = phones[idx].substring(0,3) + "-" + phones[idx].substring(3,7) + "-" + phones[idx].substring(7);
                }
            }
            if (emergencyPhones[idx].contains("-")) {
                emergencyPhone = emergencyPhones[idx];
            } else {
                if (emergencyPhones[idx].length() == 11) {
                    emergencyPhone = emergencyPhones[idx].substring(0,3) + "-" + emergencyPhones[idx].substring(3,6) + "-" + emergencyPhones[idx].substring(6);
                } else {
                    emergencyPhone = emergencyPhones[idx].substring(0,3) + "-" + emergencyPhones[idx].substring(3,7) + "-" + emergencyPhones[idx].substring(7);
                }
            }
            RideShip rideShip =  new RideShip(details, names[idx], birthdate, phone, emergencyPhone, member);
            rideShipRepository.save(rideShip);
        }

        // 쿠폰 사용
        if (reserveDTO.getCouponId() != null) {
            if (reserveDTO.getCouponId() != 0L) {
                CouponMember myCoupon = couponMemberRepository.getOne(reserveDTO.getCouponId());
                Coupon coupon = myCoupon.getCoupon();
                myCoupon.use(order);
                coupon.useCoupon();
                couponMemberRepository.save(myCoupon);
                couponRepository.save(coupon);
            }
        }

        return new OrderResponse(order.getId(),
                orderNumber,
                details.getGoods().getName(),
                reserveDTO.getPaymentPrice().intValue(),
                reserveDTO.getReservePersonName(),
                member.getEmail(),
                reserveDTO.getReservePersonPhone(),
                reserveDTO.getPayMethod());
    }

    private FishingDiarySmallResponse getDiarySmallResponse(FishingDiary diary) {
        ArrayList<String> imageUrlList = new ArrayList<>();
        String path = env.getProperty("file.downloadUrl");
        List<FileEntity> fileEntityList = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                diary.getId(), diary.getFilePublish(), FileType.image,false);
        for(int i=0; i<fileEntityList.size(); i++){
            if (i > 3) break;
            FileEntity fileEntity = fileEntityList.get(i);
            imageUrlList.add(path + "/" +fileEntity.getFileUrl() + "/" + fileEntity.getThumbnailFile());
        }
        return new FishingDiarySmallResponse(diary, imageUrlList, fileEntityList.size()-imageUrlList.size());
    }

//    public List<String> getShipPositions(Long goods_id) {
//        Goods goods = goodsRepository.getOne(goods_id);
//        Ship ship = goods.getShip();
//        List<String> positions = Arrays.stream(ship.getPositions().split(",")).collect(Collectors.toList());
//    }

    public void calcDistance() {
        List<Ship> ships = shipRepo.findAll();
        for (Ship ship : ships) {
            List<ObserverCode> codes = observerCodeRepository.findAll();
            ObserverCode code = codes.stream()
                    .sorted(Comparator.comparing(e -> e.distanceFrom(ship.getLocation())))
                    .collect(Collectors.toList())
                    .get(0);
            ship.setObserverCode(code.getCode());
            shipRepo.save(ship);
        }
    }


    public Long findAllShipCount() {
        return shipRepo.findAllByIsActive();
    }

//    @Transactional
//    public Page<ReviewDto> getShipReviews(Integer page, Long shipId) {
//
//    }

    @Transactional
    public Map<String, Object> getReviewByShip(Long shipId, Integer page, Integer size) throws EmptyListException {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> responses = reviewRepository.getShipReviews(shipId, pageable);
        List<ReviewResponse> contents = responses.getContent();
        if (contents.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        for (ReviewResponse review : contents) {
            review.setImages(fileRepo.findByPidAndFilePublishAndIsDelete(review.getId(), FilePublish.review, false).stream().map(FilesDTO::of).collect(Collectors.toList()));
        }
        Map<String, Object> result = new HashMap<>();
        Ship ship = shipRepo.getOne(shipId);
        result.put("reviews", new PageImpl<>(contents, pageable, responses.getTotalElements()));
        result.put("average", ship.getTotalAvgByReview());
        result.put("taste", ship.getTasteByReview());
        result.put("service", ship.getServiceByReview());
        result.put("clean", ship.getCleanByReview());
        return result;
    }

    @Transactional
    public ObserverCode getObserverCodeFromShip(Long shipId) {
        return observerCodeRepository.getObserverCodeByCode(shipRepo.getOne(shipId).getObserverCode());
    }

    @Transactional
    public Page<TvListResponse> getTvList(ShipSearchDTO shipSearchDTO, int page) {
        Pageable pageable;
        if (shipSearchDTO.getOrderBy().equals("")) {
            pageable = PageRequest.of(page, shipSearchDTO.getSize());
        } else {
            pageable = PageRequest.of(page, shipSearchDTO.getSize(), Sort.by(shipSearchDTO.getOrderBy()));
        }
        return shipRepo.searchTvList(shipSearchDTO, pageable);
    }

    @Transactional
    public Map<String, Object> getLiveDetail(Long shipId, Long cameraId) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        List<RealTimeVideo> videos = realTimeVideoRepository.getRealTimeVideoByShipsId(shipId);
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> cameraList = new ArrayList<>();
        if (videos.size() > 0) {
            for (RealTimeVideo video : videos) {
                Map<String, Object> c = new HashMap<>();
                if (video.getId().equals(cameraId)) {
                    Ship ship = video.getShips();
                    c.put("id", video.getId());
                    c.put("name", video.getName());
                    c.put("thumbnailUrl", "/resource" + ship.getProfileImage());
                    c.put("liveVideo", "");
                    c.put("species", ship.getFishSpecies().stream().map(CommonCode::getCodeName).collect(Collectors.joining(",")));
                    c.put("address", ship.getAddress());

                    if (video.getType().equals("toast")) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime expTime = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                                TimeZone.getDefault().toZoneId()
                        );
                        String token = "";
                        if (now.isAfter(expTime)) {
                            Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                            token = tokenData.get("token");
                            String expireTime = tokenData.get("expireTime");
                            realTimeVideoRepository.updateToken(token, expireTime, video.getToken());
                        } else {
                            token = video.getToken();
                        }
                        if (!token.equals("")) {
                            List<Map<String, Object>> cameras = httpRequestService.getCameraList(token);
                            for (Map<String, Object> camera : cameras) {
                                String serial = camera.get("serialNo").toString();
                                if (serial.equals(video.getSerial())) {
                                    String type = camera.get("recordType").toString();
                                    String streamStatus = camera.get("streamStatus").toString();
                                    String controlStatus = camera.get("controlStatus").toString();
                                    String liveUrl = "";
                                    if (type.equals("24h")) {
                                        if (streamStatus.equals("on")) {
                                            liveUrl = httpRequestService.getPlayUrl(token, serial);
                                        }
                                    } else {
                                        if (controlStatus.equals("on")) {
                                            liveUrl = httpRequestService.getPlayUrl(token, serial);
                                        }
                                    }
                                    c.put("liveVideo", liveUrl);
                                }
                            }
                        }
                    } else {
                        String token = httpRequestService.loginADT(ship.getCompany().getAdtId(), ship.getCompany().getAdtPw(), video.getId().toString());
                        String liveUrl = httpRequestService.getADTCameraLive(video.getSerial(), token);
                        c.put("liveVideo", Objects.requireNonNullElse(liveUrl, ""));
                    }
                    response.put("cameraData", c);
                } else {
                    c.put("id", video.getId());
                    c.put("name", video.getName());
                    c.put("thumbnailUrl", "/resource" + video.getShips().getProfileImage());
                    c.put("liveVideo", "");
                }
                cameraList.add(c);
            }
            response.put("cameraList", cameraList);
        }
        response.putIfAbsent("cameraData", null);
        return response;
    }
}
