package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.common.Banner;
import com.tobe.fishking.v2.entity.common.Banner;
import com.tobe.fishking.v2.model.admin.MainBannerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>{

    @Query("select b from Banner b order by b.orders")
    List<Banner> findAllOrderByOrder();

    @Query("select new com.tobe.fishking.v2.model.admin.MainBannerDto(b,f) " +
            "from Banner b join FileEntity f on f.filePublish=15 and f.pid=b.id and f.isDelete=false order by b.orders")
    List<MainBannerDto> getMainBannerList();
}