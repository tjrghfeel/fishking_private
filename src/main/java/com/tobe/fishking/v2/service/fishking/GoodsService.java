package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository repository;

    public Page<GoodsDTO> getGoodsList(Pageable pageable, Integer totalElements) {
        Page<Goods> goods = repository.findAll(pageable, totalElements);
        return goods.map(GoodsDTO::of);
    }
}