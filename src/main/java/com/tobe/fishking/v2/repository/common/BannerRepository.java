package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.Banner;
import com.tobe.fishking.v2.entity.common.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>{

}