package com.tobe.fishking.v2.model.response;

import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TidalLevelResponse {

    private Integer level;
    private String dateTime;
    private String peak;

    public TidalLevelResponse(Integer level,
                              LocalDateTime dateTime,
                              String peak) {
        this.level = level;
        this.dateTime = DateUtils.getDateTimeInFormat(dateTime);
        this.peak = peak == null ? "" : peak;
    }

}
