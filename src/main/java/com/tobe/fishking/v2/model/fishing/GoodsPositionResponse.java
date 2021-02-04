package com.tobe.fishking.v2.model.fishing;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GoodsPositionResponse {

    private List<String> positions;
    private List<String> usedPositions;
    private List<String> availablePositions;

    @Builder
    public GoodsPositionResponse(List<String> positions,
                                 List<String> usedPositions,
                                 List<String> availablePositions) {
        this.positions = positions;
        this.usedPositions = usedPositions;
        this.availablePositions = availablePositions;
    }

}
