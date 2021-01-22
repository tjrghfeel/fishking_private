package com.tobe.fishking.v2.model.admin.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriteOne2oneAnswerDto {
    private Long parentId;
    private String content;
    private Long[] fileList;
}
