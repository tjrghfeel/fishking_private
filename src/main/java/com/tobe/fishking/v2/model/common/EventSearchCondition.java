package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSearchCondition {
    private Boolean isLast;
    private String title;
    private String content;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdDateEnd;
    private String nickName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Boolean shipEvent;
    private String shipName;
    private Integer pageCount=10;
}
