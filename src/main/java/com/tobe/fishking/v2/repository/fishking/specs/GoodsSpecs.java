package com.tobe.fishking.v2.repository.fishking.specs;

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
        FISHSPECIES("fishspecies"),
        FISHSPECIESNAME("fishSpeciesName"),
        PERSONNEL("persionnel"),
        REGION("region"),

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
                case FISHSPECIESNAME:
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
                if("name".equals(key)){ //'name' ????????? like ??????
                    predicate.add(builder.like(root.get(key), "%"+searchKeyword.get(key)+"%"));
                }else if("partner".equals(key)){ // 'partner' ????????? partner?????? ?????? ?????? keword???????????? 2??? ???????????? ??????
                    Join<Item,Partner> join = root.join("partner");
                    Map<String, Object> partnerKeyword = CommonObjectUtils.convertObjectToMap(searchKeyword.get(key));
                    for (String partnerKey : partnerKeyword.keySet()) {
                        if("name".equals(partnerKey)){
                            predicate.add(builder.like(join.get("name"), "%"+ partnerKeyword.get("name")+"%"));
                        }
                    }
                }else{ // 'name', 'partner' ????????? ?????? ?????? ??????????????? ?????? equal ??????
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



