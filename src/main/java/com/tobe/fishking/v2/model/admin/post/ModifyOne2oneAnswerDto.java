package com.tobe.fishking.v2.model.admin.post;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyOne2oneAnswerDto {
    private Long answerPostId;
    private String content;
    private Long[] fileList;
}
