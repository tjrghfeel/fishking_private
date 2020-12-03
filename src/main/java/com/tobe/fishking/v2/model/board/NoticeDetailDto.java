package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class NoticeDetailDto {
    private Long id;
    private String channelType;
    private String title;
    private LocalDateTime date;
    private String contents;
    private String fileList;
}
