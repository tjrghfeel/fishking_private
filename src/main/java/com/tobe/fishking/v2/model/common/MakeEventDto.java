package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeEventDto {
    @NotNull
    @Size(min = 1,max = 100)
    private String title;
    @NotNull
    @Size(min = 1,max = 2000)
    private String content;
    private Long shipId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Integer orderLevel;
    private Long[] fileList;
}
