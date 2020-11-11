package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.common.CommonObjectUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoodsSpecs {

    public enum SearchKey {

        NAME("name"),
        FISHINGTYPE("fishingType"),
        FISHINGDATE("fishingDate"),
        FISHINGSPECIESNAME("fishingSpeciesName"),
        TAG("tag"),
        LIKESGREATERTHAN("likes");


        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Goods> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Goods>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Goods> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case NAME:
                case TAG:
                case FISHINGDATE:
                    predicate.add(builder.equal(
                            root.get(key.value), "%" + searchKeyword.get(key) + "%"
                    ));
                    break;
                case FISHINGSPECIESNAME:
                    Join<Goods, CommonCode> join = root.join("fishSpecies");
                    Map<String, Object> fishSpeciesKeyword = CommonObjectUtils.convertObjectToMap(searchKeyword.get(key));
                    for (String fishSpeciesKey : fishSpeciesKeyword.keySet()) {
                        if ("name".equals(fishSpeciesKey)) {
                            predicate.add(builder.like(join.get("name"), "%" + fishSpeciesKeyword.get("name") + "%"));
                        }
                    }
                    break;

                case FISHINGTYPE:
                case LIKESGREATERTHAN:
                    predicate.add(builder.greaterThan(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;
            }
        }



/*
            for (String key : searchKeyword.keySet()) {
                if("name".equals(key)){ //'name' 조건은 like 검색
                    predicate.add(builder.like(root.get(key), "%"+searchKeyword.get(key)+"%"));
                }else if("partner".equals(key)){ // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색
                    Join<Item,Partner> join = root.join("partner");
                    Map<String, Object> partnerKeyword = CommonObjectUtils.convertObjectToMap(searchKeyword.get(key));
                    for (String partnerKey : partnerKeyword.keySet()) {
                        if("name".equals(partnerKey)){
                            predicate.add(builder.like(join.get("name"), "%"+ partnerKeyword.get("name")+"%"));
                        }
                    }
                }else{ // 'name', 'partner' 이외의 모든 조건 파라미터에 대해 equal 검색
                    predicate.add(builder.equal(root.get(key), searchKeyword.get(key)));
                }
            }
            */


        return predicate;
    }



    public static Specification<Goods> searchAndWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Goods>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateAndWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }


    private static List<Predicate> getPredicateAndWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Goods> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {

            switch (key) {
                case FISHINGDATE:
                    predicate.add(builder.equal(
                            root.get(key.value), searchKeyword.get(key)
                    ));
                    continue;
                case FISHINGTYPE:
                    predicate.add(builder.equal(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    continue;
            }
        }
        return predicate;
    }


    public static Specification<Goods> fishingType(FishingType fishingType) {
        return new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("fishingType"), fishingType);
            }
        };
    }

    // 20201107
    public static Specification<Goods> fishingDate(String fishingDate) {
        return new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("fishingDate"), fishingDate);
            }
        };
    }


/*

    public static Specification<Goods> equalFishingDate(final String keyword) {

        return new Specification<Goods>() {

            @Override

            public Predicate toPredicate(Root<Goods> root,

                                         CriteriaQuery<?> query, CriteriaBuilder cb) {

                return cb.like(root.get(Goods.), "%" + keyword + "%");

            }

        };

    }*/

}



