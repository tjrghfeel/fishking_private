package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CameraPointRepository extends BaseRepository<CameraPoint, Long> {
    //카메라 포인트 리스트 조회. 관리자용.
    @Query(
            value="select " +
                    "   c.id, c.name, c.sido, c.gungu, c.address, c.latitude, c.longitude, c.adt_id adtId, c.adt_pw adtPw, " +
                    "   c.img_url imgUrl, c.is_active isActive " +
                    "from camera_point c " +
                    "where " +
                    "   c.is_deleted=false " ,
            countQuery = "" +
                    "select c.id " +
                    "from camera_point c " +
                    "where " +
                    "   c.is_deleted=false " ,
            nativeQuery = true
    )
    Page<CameraPointDetailDtoForPage> getAllByPageForManager(Pageable pageable);

    //카메라 포인트 리스트 조회. 일반용.
    @Query(
            value="select " +
                    "   c.id, c.name, c.sido, c.gungu, c.address, c.latitude, c.longitude, c.adt_id adtId, c.adt_pw adtPw, c.img_url imgUrl " +
                    "from camera_point c " +
                    "where " +
                    "   c.is_deleted=false " +
                    "   and c.is_active=true " ,
            countQuery = "" +
                    "select c " +
                    "from camera_point c " +
                    "where c    " +
                    "   c.is_deleted=false " +
                    "   and c.is_active=true " ,
            nativeQuery = true
    )
    List<CameraPointDetailDtoForPage> getAllForMainPage();

}
