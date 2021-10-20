package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.entity.fishing.Harbor;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CameraPointRepository extends BaseRepository<CameraPoint, Long> {

    @Query(
            value="select " +
                    "c.name, c.sido, c.gungu, c.address, c.latitude, c.longitude, c.adt_id, c.adt_pw " +
                    "from camera_point c " +
                    "",
            countQuery = "" +
                    "select " +
                    "   c.name, c.sido, c.gungu, c.address, c.latitude, c.longitude, c.adt_id, c.adt_pw " +
                    "from camera_point c " +
                    "",
            nativeQuery = true
    )
    Page<CameraPointDetailDtoForPage> getAllByPage(Pageable pageable);

}
