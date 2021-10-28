package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CameraPoint;
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

    @Query("select v from RealTimeVideo v where v.ships.company.member = :member")
    List<RealTimeVideo> getRealTimeVideoByMember(Member member);

    @Query("select v from RealTimeVideo v where v.ships.id = :ship_id and v.type = 'toast'")
    List<RealTimeVideo> getNHNByShipsId(Long ship_id);

    @Query("select v from RealTimeVideo v where v.ships.company.member.id = :memberId and v.type = 'toast'")
    List<RealTimeVideo> getNHNByMemberId(Long memberId);

    @Query("select v from RealTimeVideo v where v.ships.id = :ship_id and v.type = 'caps'")
    List<RealTimeVideo> getADTByShipsId(Long ship_id);

    @Query("select v from RealTimeVideo v where v.ships.company.member.id = :memberId and v.type = 'caps'")
    List<RealTimeVideo> getADTByMemberId(Long memberId);

    @Modifying
    @Query("update RealTimeVideo v set v.isUse = false where v in :videos")
    void setNotUse(List<RealTimeVideo> videos);

    @Query("select v from RealTimeVideo v where v.ships.nhnId = :nhnId and v.type = 'toast'")
    List<RealTimeVideo> getNHNByNHNId(String nhnId);

    @Modifying
    @Query("update RealTimeVideo v set v.token = :newToken, v.expireTime = :time where v.ships.id = :shipId")
    void updateToken(String newToken, String time, Long shipId);

}
