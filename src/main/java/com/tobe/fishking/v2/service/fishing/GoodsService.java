package com.tobe.fishking.v2.service.fishing;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.repository.fishing.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    public List<Object[]> getCountTotalGoodsByFishSpecies(){
        return goodsRepository.countTotalGoodsByFishSpecies();
       // return null;
    }

    public List<Object[]> getCountTotalGoodsByRegion(){
        return goodsRepository.countTotalGoodsByRegion();
    }


    public Page<GoodsDTO> getGoods(Pageable pageable, Integer totalElements) {
        Page<Goods> customers = goodsRepository.findAll(pageable, totalElements);
        return customers.map(GoodsDTO::of);
    }
/*

    @Transactional(readOnly = true)
    public Page<GoodsResponseDto> searchAll(GoodsSearchDTO search,Pageable pageable){
        return goodsRepository.searchAll(search,pageable)
                .map(GoodsResponseDto::new);
    }
*/


}
