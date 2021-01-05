package com.tobe.fishking.v2.model.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqWriteDto {
    private String questionType;
    private String title;
    private String contents;
    private Long[] fileList;
}
