package com.tobe.fishking.v2.model.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeUpdateDto {
    private Long postId;
    private String channelType;
    private String title;
    private String contents;
    private Long[] fileList;
    private String targetRole;
}