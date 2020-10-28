package com.tobe.fishking.v2.service.fishing;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.repository.fishing.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
