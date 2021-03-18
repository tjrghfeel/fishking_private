package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FishingDashboardResponse {

    private Long countRunning;
    private Long countConfirm;
    private Double percentRunning;
    private Double percentConfirm;

    private Long countWait;
    private Long countFix;
    private Double percentWait;
    private Double percentFix;

    private Long countCancel;
    private Long countComplete;
    private Double percentCancel;
    private Double percentComplete;

    @QueryProjection
    public FishingDashboardResponse(Long countRunning,
                                    Long countConfirm,
                                    Long countWait,
                                    Long countFix,
                                    Long countCancel,
                                    Long countComplete) {
        this.countRunning = countRunning;
        this.countConfirm = countConfirm;
        this.percentRunning = countRunning * 100.0 / (countRunning + countConfirm);
        this.percentConfirm = countConfirm * 100.0 / (countRunning + countConfirm);

        this.countWait = countWait;
        this.countFix = countFix;
        this.percentWait = countWait * 100.0 / (countWait + countFix);
        this.percentFix = countFix * 100.0 / (countWait + countFix);

        this.countCancel = countCancel;
        this.countComplete = countComplete;
        this.percentCancel = countCancel * 100.0 / (countCancel + countComplete);
        this.percentComplete = countComplete * 100.0 / (countCancel + countComplete);
    }
}
