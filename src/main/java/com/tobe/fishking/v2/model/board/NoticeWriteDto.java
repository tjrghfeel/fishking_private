package com.tobe.fishking.v2.model.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeWriteDto {
    private String channelType;
    private String title;
    private String contents;
    private Long[] fileList;
    private String targetRole;
}
