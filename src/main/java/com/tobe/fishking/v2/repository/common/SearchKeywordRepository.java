package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.SearchKeyword;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchKeywordRepository extends BaseRepository<SearchKeyword, Long>, SearchKeywordRepositoryCustom {

    @Modifying
    @Query("update SearchKeyword k set k.popular = false where k.popular = true")
    void resetPopular();

    @Query("select k from SearchKeyword k where k.searchKeyword = :keyword")
    Optional<SearchKeyword> getSearchKeywordBySearchKeyword(String keyword);

}
