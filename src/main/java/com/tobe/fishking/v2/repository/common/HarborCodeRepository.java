package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.common.HarborCode;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarborCodeRepository extends BaseRepository<HarborCode, Long> {

    @Query("select h from HarborCode h where h.name like %:name% and h.dong like %:dong%")
    List<HarborCode> findAllByNameAndDong(String name, String dong);

}
