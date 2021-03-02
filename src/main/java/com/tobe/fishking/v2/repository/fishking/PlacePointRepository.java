package com.tobe.fishking.v2.repository.fishking;

import com.tobe.fishking.v2.entity.fishing.PlacePoint;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacePointRepository extends BaseRepository<PlacePoint, Long> {

    @Query("select p from PlacePoint p where p.place = :place")
    List<PlacePoint> getPlacePointByPlace(Places place);

}
