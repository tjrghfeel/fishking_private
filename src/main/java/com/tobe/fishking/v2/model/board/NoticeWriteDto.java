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
public class NoticeWriteDto {
    private String channelType;
    @Size(min=1, max=100)
    private String title;
    @Size(min=1, max=2000)
    private String contents;
    private Long[] fileList;
    private String targetRole;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate noticeStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate noticeEndDate;

//    public NoticeWriteDto(
//            String channelType, String title, String contents, Long[] fileList, String targetRole,
//            LocalDate noticeStartDate, LocalDate noticeEndDate
//    ){
//        //입력값 검증
//        if(noticeStartDate.isAfter(noticeEndDate)){throw new RuntimeException("공지 시작일은 종료일 이전이어야 합니다.");}
//
//        this.channelType = channelType;
//        this.title = title;
//        this.contents = contents;
//        this.fileList = fileList;
//        this.targetRole = targetRole;
//        this.noticeStartDate = noticeStartDate;
//        this.noticeEndDate = noticeEndDate;
//    }
}
