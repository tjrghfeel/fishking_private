package com.tobe.fishking.v2.repository.fishing.specs;


import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FishingDiarySpecs {

    public enum SearchKey {
        TITLE("title"),
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

    public static Specification<FishingDiary> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<FishingDiary>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<FishingDiary> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case TITLE:
                case TAG:
                    predicate.add(builder.equal(
                            root.get(key.value),searchKeyword.get(key)
                    ));
                    break;

                case LIKESGREATERTHAN:
                    predicate.add(builder.greaterThan(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;
            }
        }
        return predicate;
    }
    

}
