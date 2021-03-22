package com.tobe.fishking.v2.model.board;

import lombok.*;

import javax.validation.constraints.Size;

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
}
