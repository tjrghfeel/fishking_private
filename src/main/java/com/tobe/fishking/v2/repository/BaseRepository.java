package com.tobe.fishking.v2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID> {
    /*Page<T> findAll(Pageable pageable, Integer totalElements);
    Page<T> findAll(Specification<T> spec, Pageable pageable, Integer totalElements);
    Page<T> findAll(Specification<T> spec, Pageable pageable, Integer totalElements, Sort sort);*/
}