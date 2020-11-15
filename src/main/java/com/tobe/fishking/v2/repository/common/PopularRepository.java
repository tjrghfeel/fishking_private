package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PopularRepository extends BaseRepository<Popular, Long> {


    @Query(value="SELECT searchKeyword,  count(a) as cnt  FROM Popular  Where SearchPublish = :searchPublish group by  searchKeyword order by cnt desc ", nativeQuery = true)
    List<Object>  findRankListBySearchKeyWord(SearchPublish searchPublish);

}

