package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.model.fishing.GoodsSpecs;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.specs.FishingDiarySpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CommonService {

    private final FileRepository fileRepo;
    private final MemberRepository memberRepo;
    private final PopularRepository popularRepo;


    //검색 --
    public Page<FilesDTO> getFilesList(Pageable pageable,
                                       @RequestParam(required = false) Map<String, Object> searchRequest
                                           , Integer totalElement) {


        //popularRepo.save(new Popular(SearchPublish.FISHINGDIARY,  (String)searchRequest.get(key), memberRepo.getOne((long)5)));

        Page<FileEntity> files = null;
        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {

            files = searchRequest.isEmpty()
                    ? fileRepo.findAll(pageable, totalElement)
                    : fileRepo.findAllFilesWithPaginationNative((String)searchRequest.get(key), pageable);


            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }


        return files.map(FilesDTO::of);


/*

        Map<GoodsSpecs.SearchKey, Object> searchKeys = new HashMap<>();

        for (String key : searchRequest.keySet()) {
            searchKeys.put(GoodsSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        Page<FileEntity> files = searchKeys.isEmpty()
                    ? fileRepo.findAll(pageable, totalElement)
                    : fileRepo.findAll(FishingDiarySpecs.searchWith(searchKeys), pageable, totalElement);
        }

        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }


        return goods.map(GoodsDTO::of);
*/



    }



}
