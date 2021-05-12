package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.Banner;
import com.tobe.fishking.v2.entity.common.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>{

    @Query("select b from Banner b order by b.orders")
    List<Banner> findAllOrderByOrder();
}