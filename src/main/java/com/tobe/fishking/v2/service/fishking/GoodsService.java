
package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.exception.*;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.model.police.PoliceGoodsResponse;
import com.tobe.fishking.v2.model.police.RiderResponse;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.model.response.GoodsSmallResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.repository.fishking.specs.GoodsSpecs;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tobe.fishking.v2.repository.fishking.specs.RideShipSpecs.goodsIdEqu;
import static org.springframework.data.jpa.domain.Specification.where;


@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepo;
    private final MemberRepository memberRepo;
    private final ShipRepository shipRepo;  //JpaResposistory
    private final PlacesRepository placesRepo;  //JpaResposistory
    private final CommonCodeRepository commonCodeRepo;  //JpaResposistory
    private final CodeGroupRepository codeGroupRepo;  //JpaResposistory
    private final PopularRepository popularRepo;  //JpaResposistory
    private final CommonCodeRepository codeRepository;
    private final GoodsFishingDateRepository goodsFishingDateRepository;
    private final RideShipRepository rideShipRepository;


    private static int searchSize = 0;

    public Page<GoodsDTO.GoodsDTOResp> getGoodsList(Pageable pageable, Integer totalElements) {
        Page<Goods> goods = goodsRepo.findAll(pageable, totalElements);
        return goods.map(GoodsDTO.GoodsDTOResp::of);
    }


    public Page<GoodsDTO.GoodsDTOResp> getGoodsByRecommend(Pageable pageable, Integer totalElements) {
        Page<Goods> goods = goodsRepo.findAllByIsRecommend(pageable, totalElements);
        return goods.map(GoodsDTO.GoodsDTOResp::of);
    }

    //검색 --
    public Page<GoodsDTO.GoodsDTOResp> getGoodsList(Pageable pageable,
                                       @RequestParam(required = false) Map<String, Object> searchRequest,   ///total를 제외한 모든 것 조회
                                       Integer totalElement) {
        searchSize = 0;

        Map<GoodsSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (String key : searchRequest.keySet()) {
            searchKeys.put(GoodsSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
            searchSize += 1;
        }

        Page<Goods> goods = null;

        if ( searchSize > 1)  {
            goods = searchKeys.isEmpty()
                    ? goodsRepo.findAll(pageable, totalElement)
                    : goodsRepo.findAll(GoodsSpecs.searchAndWith(searchKeys), pageable, totalElement);
        }
        else {
            //어종?
            goods = searchKeys.isEmpty()
                    ? goodsRepo.findAll(pageable, totalElement)
                    : goodsRepo.findAll(GoodsSpecs.searchWith(searchKeys), pageable, totalElement);
        }

        ParamsPopular paramsPopular;


        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }


        return goods.map(GoodsDTO.GoodsDTOResp::of);
    }

    //검색 --
    public Page<GoodsDTO.GoodsDTOResp> getGoodsListLike(Pageable pageable,
                                           @RequestParam(required = false) String searchRequest,
                                           Integer totalElement) {

        Page<Goods> goods = searchRequest.isEmpty()
                    ? goodsRepo.findAll(pageable, totalElement)
                    : goodsRepo.findGoodsByFishSpeciesIsContaining(searchRequest, pageable, totalElement);

        popularRepo.save(new Popular(SearchPublish.TOTAL, searchRequest, memberRepo.getOne((long)5)));

        return goods.map(GoodsDTO.GoodsDTOResp::of);
    }


/*

    //검색 -- and
    public Page<GoodsDTO> getGoodsAndList(Pageable pageable, OperatorType orType,
                                              @RequestParam(required = false) Map<String,
                                               Object> searchRequest, Integer totalElement) {

        Map<GoodsSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (String key : searchRequest.keySet()) {
            searchKeys.put(GoodsSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        //어종?
        Page<Goods> goods = searchKeys.isEmpty()
                ? goodsRepo.findAll(pageable, totalElement)
                : goodsRepo.findAll(GoodsSpecs.searchAndWith(searchKeys), pageable, totalElement);

        //어종?
        Page<Goods> goods = searchKeys.isEmpty()
                ? goodsRepo.findAll(pageable, totalElement)
                : goodsRepo.findAll(GoodsSpecs.searchAndWith(searchKeys), pageable, totalElement);



        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }

        return goods.map(GoodsDTO::of);
    }
*/




    public Goods getGoods(long goodsId) {
        return goodsRepo.findById(goodsId).orElseThrow(CResourceNotExistException::new);
    }
    public Ship getShip(long shipId) {
        return shipRepo.findById(shipId).orElseThrow(CResourceNotExistException::new);
    }
    public Places getPlaces(long placesId) {
        return placesRepo.findById(placesId).orElseThrow(CResourceNotExistException::new);
    }


    public List<Object> getCountTotalGoodsByFishSpecies() {
        return goodsRepo.countTotalGoodsByFishSpecies();
        // return null;
    }

    public List<Object> getCountTotalGoodsByRegion() {

        return goodsRepo.countTotalGoodsByRegion();
    }

//    public Goods writeGoods(String uid, long shipId, ParamsGoods paramsGoods) {
//
//        CodeGroup cgSpecies =  codeGroupRepo.findByCode("fishspecies");
//
//        List<CommonCode> fishSpecies = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgSpecies, paramsGoods.getFishSpecies() );
//
//        CodeGroup cgLures =  codeGroupRepo.findByCode("fishinglure");
//
//        List<CommonCode> fishingLures = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgLures, paramsGoods.getFishSpecies() );
//
//
//        Goods goods = new Goods(memberRepo.findByUid(uid).orElseThrow(CMemberNotFoundException::new), shipRepo.findById(shipId).orElseThrow(CResourceNotExistException::new), paramsGoods);
//
//        goods.setFishSpecies(fishSpecies);
//        goods.setFishSpecies(fishingLures);
//
//
//        return goodsRepo.save(goods);
//    }


    // 게시글을 수정합니다. 게시글 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    //@CachePut(value = CacheKey.POST, key = "#postId") 갱신된 정보만 캐시할경우에만 사용!
    
//    public Goods updateGoods(long goodsId, String uid, long shipId, long placesId, ParamsGoods paramsGoods) {
//        Goods goods = getGoods(goodsId);
//        Ship ship = getShip(shipId);
//        Places places = getPlaces(placesId);
//
//        Member member = goods.getCreatedBy();
//        if (!uid.equals(member.getUid()))
//            throw new CNotOwnerException();
//
//        // 영속성 컨텍스트의 변경감지(dirty checking) 기능에 의해 조회한 Goods내용을 변경만 해도 Update쿼리가 실행됩니다.
//        goods.setUpdate(member, ship, places, paramsGoods);
//        return goods;
//    }



    // 게시글을 삭제합니다. 게시글 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    public boolean deleteGoods(long goodsId, String uid) {
        Goods goods = getGoods(goodsId);
        Member member = goods.getCreatedBy();
        if (!uid.equals(member.getUid()))
            throw new CNotOwnerException();


        goods.getFishingLures().removeAll(goods.getFishingLures());
        goods.getFishSpecies().removeAll(goods.getFishSpecies());

        goodsRepo.delete(goods);

        return true;
    }

    @Transactional
    public Page<FishingShipResponse> getGoods(Long memberId, String keywordType, String keyword, String status, Integer page) throws EmptyListException {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("shipName").ascending());
        Page<FishingShipResponse> ships = shipRepo.getShipsByCompanyMember(memberId, keywordType, keyword, status, pageable);
        List<FishingShipResponse> contents = ships.getContent();
        if (contents.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        for (FishingShipResponse ship : contents) {
            List<GoodsSmallResponse> goods = goodsRepo.searchGoods(ship.getId(), keyword, status);
            ship.setGoodsList(goods);
        }
        return new PageImpl<>(contents, pageable, ships.getTotalElements());
    }

    @Transactional
    public UpdateGoodsResponse getGoodsData(Long goodsId) {
        return goodsRepo.getGoodsData(goodsId);
    }

    @Transactional
    public Long addGood(AddGoods addGoods, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: " + token));
        Ship ship = shipRepo.getOne(addGoods.getShipId());

        List<CommonCode> species = new ArrayList<>();
        for (String species_code : addGoods.getSpecies()) {
//        for (String species_code : speciesList) {
            CommonCode commonCode = codeRepository.getByCode(species_code);
            species.add(commonCode);
        }

        Goods goods = Goods.builder()
                .fishSpecies(species)
                .ship(ship)
                .member(member)
                .addGoods(addGoods)
                .build();
        goodsRepo.save(goods);

        for (String fishingDate : addGoods.getFishingDates()) {
//        for (String fishingDate : fishingDatesList) {
            GoodsFishingDate goodsFishingDate = GoodsFishingDate.builder()
                    .goods(goods)
                    .fishingDateString(fishingDate)
                    .member(member)
                    .build();
            goodsFishingDateRepository.save(goodsFishingDate);
        }

        if (addGoods.getAmount() < ship.getCheapestGoodsCost()) {
            ship.changeCheapest(member, addGoods.getAmount());
            shipRepo.save(ship);
        }
        return goods.getId();
    }

    @Transactional
    public boolean updateGoods(Long goodsId, UpdateGoods updateGoods, Member member) {
        Goods goods = goodsRepo.getOne(goodsId);
        Ship ship = shipRepo.getOne(updateGoods.getShipId());
        List<CommonCode> species = new ArrayList<>();
        for (String species_code : updateGoods.getSpecies()) {
//        for (String species_code : speciesList) {
            CommonCode commonCode = codeRepository.getByCode(species_code);
            species.add(commonCode);
        }
        goods.updateGoods(ship, member, updateGoods, species);
        goodsRepo.save(goods);

        List<GoodsFishingDate> oldDates = goodsFishingDateRepository.findAllByGoodsId(goodsId);
        List<String> fishingDatesList = updateGoods.getFishingDates();
        for (GoodsFishingDate o : oldDates) {
            if (fishingDatesList.contains(o.getFishingDateString())) {
                fishingDatesList.remove(o.getFishingDateString());
            } else {
                goodsFishingDateRepository.delete(o);
            }
        }

        for (String fishingDate : fishingDatesList) {
//        for (String fishingDate : fishingDatesList) {
            GoodsFishingDate goodsFishingDate = GoodsFishingDate.builder()
                    .goods(goods)
                    .fishingDateString(fishingDate)
                    .member(member)
                    .build();
            goodsFishingDateRepository.save(goodsFishingDate);
        }

        return true;
    }

    @Transactional
    public Long getTodayRunGoods() {
        return goodsRepo.getTodayRunGoods();
    }

    @Transactional
    public Long getNowRunGoods() {
        return goodsRepo.getNowRunGoods();
    }

    @Transactional
    public Long getWaitRidePersonnel() {
        return goodsRepo.getWaitRidePersonnel();
    }

    @Transactional
    public Long getRealRidePersonnel() {
        return goodsRepo.getRealRidePersonnel();
    }

    @Transactional
    public List<PoliceGoodsResponse> getPoliceAllGoods() {
        return goodsRepo.getPoliceAllGoods();
    }

    @Transactional
    public Page<PoliceGoodsResponse> getPoliceAllGoods(Integer page) {
        return goodsRepo.getPoliceGoods(page);
    }

    @Transactional
    public Map<String, Object> getRideData(Long goodsId) {
        Map<String, Object> result = new HashMap<>();

        result.put("shipName", shipRepo.getShipNameByGoodsId(goodsId));
        result.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)")));

        List<RiderResponse> response = goodsRepo.getRiderData(goodsId);
        result.put("riders", response);
        result.put("ridersCount", response.size());

        return result;
    }
}
