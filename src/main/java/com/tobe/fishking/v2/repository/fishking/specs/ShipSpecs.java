package com.tobe.fishking.v2.repository.fishking.specs;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipSpecs {

    public enum SearchKey {

        NAME("name"),
        FISHINGTYPE("fishingType"),
        FISHINGDATE("fishingDate"),

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

    public static Specification<Ship> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Ship>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Ship> root, CriteriaBuilder builder) {
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
                case FISHINGTYPE:
                case LIKESGREATERTHAN:
                    predicate.add(builder.greaterThan(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;
            }
        }


        return predicate;
    }


    public static Specification<Ship> searchAndWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Ship>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateAndWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }


    private static List<Predicate> getPredicateAndWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Ship> root, CriteriaBuilder builder) {
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


    public static Specification<Ship> fishingType(FishingType fishingType) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("fishingType"), fishingType);
            }
        };
    }

}



