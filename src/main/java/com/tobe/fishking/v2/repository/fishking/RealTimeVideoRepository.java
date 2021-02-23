package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealTimeVideoRepository extends BaseRepository<RealTimeVideo, Long> {

    @Query("select v from RealTimeVideo v where v.ships.id = :ship_id")
    List<RealTimeVideo> getRealTimeVideoByShipsId(Long ship_id);

    @Modifying
    @Query("update RealTimeVideo v set v.token = :newToken, v.expireTime = :time where v.token = :beforeToken")
    void updateToken(String newToken, String time, String beforeToken);

}
