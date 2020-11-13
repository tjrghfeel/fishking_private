package com.tobe.fishking.v2.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;

public class BaseRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements BaseRepository<T,ID> {

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> findAll(Pageable pageable, Integer totalElements) {
        return findAll(null, pageable, totalElements, Sort.unsorted());
    }


    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, Integer totalElements) {
        return findAll(spec, pageable, totalElements, Sort.unsorted());
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, Integer totalElements, Sort sort) {
        TypedQuery<T> query = getQuery(spec, sort);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        if(totalElements == null){
            return findAll(spec, pageable);
        }

        if(pageNumber < 0){
            throw new IllegalArgumentException("page number must not be less than zero");
        }

        if(pageSize < 1){
            throw new IllegalArgumentException("pageSize must not be less than one");
        }

        if(totalElements < pageNumber * pageSize){
            throw new IllegalArgumentException("totalElements must not be less than pageNumber * pageSize");
        }

        int offset = (int) pageable.getOffset();
        query.setFirstResult(offset);
        query.setMaxResults(pageable.getPageSize());

        return  new PageImpl<>(query.getResultList(), PageRequest.of(pageNumber, pageSize), totalElements);
    }
}