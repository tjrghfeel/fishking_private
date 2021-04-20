package com.tobe.fishking.v2.model.fishing;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TvListResponse {

    private Long id;
    private String shipName;
    private String thumbnailUrl;
    private Long cameraId;

    @QueryProjection
    public TvListResponse(
            Double distance,
            Ship ship) {
        this.id = ship.getId();
        this.shipName = ship.getShipName();
        this.thumbnailUrl = "/resource/" + ship.getProfileImage().split("/")[1]+"/thumb_"+ship.getProfileImage().split("/")[2];
        List<RealTimeVideo> video = ship.getShiipRealTimeVideos();
        this.cameraId = video.size() > 0 ? video.get(0).getId() : null;
    }
}
