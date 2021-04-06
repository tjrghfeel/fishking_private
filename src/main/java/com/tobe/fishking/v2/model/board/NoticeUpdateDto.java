package com.tobe.fishking.v2.model.board;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeUpdateDto {
    private Long postId;
    private String channelType;
    @Size(min=1, max=100)
    private String title;
    @Size(min=1, max=2000)
    private String contents;
    private Long[] fileList;
    private String targetRole;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate noticeEndDate;
}