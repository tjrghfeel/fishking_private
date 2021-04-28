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
    @Size(min = 1, message = "제목을 입력하세요")
    @Size(max=50, message = "제목은 50자 이하이어야 합니다.")
    private String title;
    @NotNull
    @Size(min = 1,message = "내용을 입력하세요")
    @Size(max=2000, message = "내용은 2000자 이하이어야 합니다")
    private String content;
    private Long shipId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private Integer orderLevel;
    private Long[] fileList;
}
