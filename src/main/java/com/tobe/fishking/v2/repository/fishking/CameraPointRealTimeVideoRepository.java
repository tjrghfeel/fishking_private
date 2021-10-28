package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.entity.fishing.CameraPointRealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraPointRealTimeVideoRepository extends BaseRepository<CameraPointRealTimeVideo, Long> {

    @Query("select v from CameraPointRealTimeVideo v where v.cameraPoint.id = :cameraPoint_id")
    List<CameraPointRealTimeVideo> getRealTimeVideoByShipsId(Long cameraPoint_id);

    @Modifying
    @Query("update CameraPointRealTimeVideo v set v.token = :newToken, v.expireTime = :time where v.token = :beforeToken")
    void updateToken(String newToken, String time, String beforeToken);


    @Query("select v from CameraPointRealTimeVideo v where v.cameraPoint.id = :cameraPoint_id and v.type = 'toast'")
    List<CameraPointRealTimeVideo> getNHNByShipsId(Long cameraPoint_id);

//    @Query("select v from CameraPointRealTimeVideo v where v.ships.company.member.id = :memberId and v.type = 'toast'")
//    List<CameraPointRealTimeVideo> getNHNByMemberId(Long memberId);

    @Query("select v from CameraPointRealTimeVideo v where v.cameraPoint.id = :cameraPoint_id and v.type = 'caps'")
    List<CameraPointRealTimeVideo> getADTByShipsId(Long cameraPoint_id);

//    @Query("select v from CameraPointRealTimeVideo v where v.ships.company.member.id = :memberId and v.type = 'caps'")
//    List<CameraPointRealTimeVideo> getADTByMemberId(Long memberId);

    @Modifying
    @Query("update CameraPointRealTimeVideo v set v.isUse = false where v in :videos")
    void setNotUse(List<CameraPointRealTimeVideo> videos);

    @Query("select v from CameraPointRealTimeVideo v where v.cameraPoint.adtId = :nhnId and v.type = 'toast'")
    List<CameraPointRealTimeVideo> getNHNByNHNId(String nhnId);

    @Modifying
    @Query("update CameraPointRealTimeVideo v set v.token = :newToken, v.expireTime = :time where v.cameraPoint.id = :cameraPointId")
    void updateToken(String newToken, String time, Long cameraPointId);

    List<CameraPointRealTimeVideo> findAllByCameraPoint(CameraPoint cameraPoint);
}

