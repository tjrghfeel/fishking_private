package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface PopularRepository extends BaseRepository<Popular, Long> {

}
