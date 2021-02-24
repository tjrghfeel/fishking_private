package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.Event;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ShipEventResponse {

    private Long id;
    private String title;

    @Builder
    public ShipEventResponse (Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @Builder
    public ShipEventResponse (Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
    }

}
