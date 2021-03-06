package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.common.SearchKeyword;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.common.PopKeywordResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularService {
    private final CommonCodeRepository commonCodeRepo;
    private final CodeGroupRepository codeGroupRepo;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final PopularRepository popularRepo;
    private final SearchKeywordRepository searchKeywordRepository;


    //검색 --
    public  List<Object> getPopularList(@RequestParam(required = false) SearchPublish searchPublish) {

        List<Object> populars =  popularRepo.findRankListBySearchKeyWord(searchPublish);

        return populars;

    }

    /* 인기검색어 */
    @Transactional
    public List<PopKeywordResponse> getPopularKeyword() {
//        return searchKeywordRepository.getPopular().stream().map(SearchKeyword::getSearchKeyword).collect(Collectors.toList());
        return searchKeywordRepository.getPopularKeywordResponses();
    }

    /* 인기검색어 */
    @Transactional
    public List<String> getPopularKeywordString() {
        return searchKeywordRepository.getPopularKeywordResponses().stream().map(PopKeywordResponse::getKeyword).collect(Collectors.toList());
    }

    @Transactional
    public void updatePopularKeyword() {
        List<SearchKeyword> populars = searchKeywordRepository.getPopular();
        List<SearchKeyword> before = searchKeywordRepository.getPopularKeywords();
        for (SearchKeyword keyword : populars) {
            if (keyword.getPopular()) {
                keyword.isNotNew();
            } else {
                keyword.isNew();
            }
        }
        for (SearchKeyword keyword : before) {
            keyword.isNotPopular();
        }
        for (SearchKeyword keyword : populars) {
            keyword.isPopular();
        }
        searchKeywordRepository.saveAll(populars);
        searchKeywordRepository.saveAll(before);
    }
}
