package com.tobe.fishking.v2.model.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QnaWriteDto {
    private String questionType;
    private String contents;
    private String returnType;
    private String returnAddress;
    private Long[] fileList;
    private String targetRole;
}
