package com.tobe.fishking.v2.repository.fishking.specs;


import com.tobe.fishking.v2.entity.FileEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FilesSpecs {

    public enum SearchKey {
        /*
          날짜
          어종
          인원
          지역
          편의시설
          주변시설
          정렬
         */
        CONTENTS("contents");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<FileEntity> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<FileEntity>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<FileEntity> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case CONTENTS:  //String
                    predicate.add(builder.equal(
                            root.get(key.value),searchKeyword.get(key)
                    ));
                    break;/*
                case PERSONNEL:   //integer
                case REGION:
                case LIKESGREATERTHAN:
                    predicate.add(builder.greaterThan(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;*/

            }
        }
        return predicate;
    }

    public static Specification<FileEntity> searchLikeWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<FileEntity>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateLikeWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateLikeWithKeyword(Map<SearchKey, Object> searchKeyword, Root<FileEntity> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case CONTENTS:  //String
                    predicate.add(builder.like(
                            root.get(key.value),searchKeyword.get(key).toString() + "%"
                    ));
                    break;/*
                case PERSONNEL:   //integer
                case REGION:
                case LIKESGREATERTHAN:
                    predicate.add(builder.greaterThan(
                            root.get(key.value), Integer.valueOf(searchKeyword.get(key).toString())
                    ));
                    break;*/

            }
        }
        return predicate;
    }

    

}
