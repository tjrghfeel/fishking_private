
package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.exception.CMemberNotFoundException;
import com.tobe.fishking.v2.exception.CNotOwnerException;
import com.tobe.fishking.v2.exception.CResourceNotExistException;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.model.fishing.ParamsPopular;
import com.tobe.fishking.v2.repository.fishking.specs.GoodsSpecs;
import com.tobe.fishking.v2.model.fishing.ParamsGoods;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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




    public Page<GoodsDTO> getGoodsList(Pageable pageable, Integer totalElements) {
        Page<Goods> goods = goodsRepo.findAll(pageable, totalElements);
        return goods.map(GoodsDTO::of);
    }


    public Page<GoodsDTO> getGoodsByRecommend(Pageable pageable, Integer totalElements) {
        Page<Goods> goods = goodsRepo.findAllByRecommend(pageable, totalElements);
        return goods.map(GoodsDTO::of);
    }

    //검색 --
    public Page<GoodsDTO> getGoodsList(Pageable pageable,OperatorType orType,
                                       @RequestParam(required = false) Map<String, Object> searchRequest,   ///total를 제외한 모든 것 조회
                                       Integer totalElement) {

        Map<GoodsSpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (String key : searchRequest.keySet()) {
            searchKeys.put(GoodsSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        Page<Goods> goods = null;

        if (OperatorType.and == orType)
        {
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


        return goods.map(GoodsDTO::of);
    }

    //검색 --
    public Page<GoodsDTO> getGoodsListLike(Pageable pageable,
                                           @RequestParam(required = false) String searchRequest,
                                           Integer totalElement) {

        Page<Goods> goods = searchRequest.isEmpty()
                    ? goodsRepo.findAll(pageable, totalElement)
                    : goodsRepo.findGoodsByFishSpeciesIsContaining(searchRequest, pageable, totalElement);

        popularRepo.save(new Popular(SearchPublish.TOTAL, searchRequest, memberRepo.getOne((long)5)));

        return goods.map(GoodsDTO::of);
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

    public Goods writeGoods(String uid, long shipId, ParamsGoods paramsGoods) {

        CodeGroup cgSpecies =  codeGroupRepo.findByCode("fishspecies");

        List<CommonCode> fishSpecies = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgSpecies, paramsGoods.getFishSpecies() );

        CodeGroup cgLures =  codeGroupRepo.findByCode("fishinglure");

        List<CommonCode> fishingLures = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgLures, paramsGoods.getFishSpecies() );


        Goods goods = new Goods(memberRepo.findByUid(uid).orElseThrow(CMemberNotFoundException::new), shipRepo.findById(shipId).orElseThrow(CResourceNotExistException::new), paramsGoods);

        goods.setFishSpecies(fishSpecies);
        goods.setFishSpecies(fishingLures);


        return goodsRepo.save(goods);
    }


    // 게시글을 수정합니다. 게시글 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    //@CachePut(value = CacheKey.POST, key = "#postId") 갱신된 정보만 캐시할경우에만 사용!
    
    public Goods updateGoods(long goodsId, String uid, long shipId, long placesId, ParamsGoods paramsGoods) {
        Goods goods = getGoods(goodsId);
        Ship ship = getShip(shipId);
        Places places = getPlaces(placesId);

        Member member = goods.getCreatedBy();
        if (!uid.equals(member.getUid()))
            throw new CNotOwnerException();

        // 영속성 컨텍스트의 변경감지(dirty checking) 기능에 의해 조회한 Goods내용을 변경만 해도 Update쿼리가 실행됩니다.
        goods.setUpdate(member, ship, places, paramsGoods);
        return goods;
    }



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
    


/*
    public Optional<Goods>  getGoods(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new ResourceNotFoundException("Goods not found for this id :: " + goodsId));
    }*/






}
