package com.tobe.fishking.v2.repository.fishing;


import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingDiaryRepository extends JpaRepository<FishingDiary, Long>, JpaSpecificationExecutor<FishingDiary> {
    // List<FishingDiary> findAllByTitle(String title);
    //List<FishingDiary> findAllByTag(String tag) ;
    //List<FishingDiary> findAllByLikesGreaterThan(int likes);

    //paging
     Page<FishingDiary> findAll(Specification<FishingDiary> spec, Pageable pageable);


}

