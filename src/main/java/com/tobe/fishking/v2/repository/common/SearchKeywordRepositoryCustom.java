package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.SearchKeyword;

import java.util.List;

public interface SearchKeywordRepositoryCustom {
    List<SearchKeyword> getPopular();
}
