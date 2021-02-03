package com.tobe.fishking.v2.model.common;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyReviewDto {
    @NotNull
    private Long reviewId;
//    @NotNull
//    private Long goodsId;
    @NotNull(message = "청결도 점수를 입력해야 합니다. ")
    @Min(0)
    @Max(5)
    private Double cleanScore;
    @NotNull(message = "서비스 점수를 입력해야 합니다.")
    @Min(0)
    @Max(5)
    private Double serviceScore;
    @NotNull(message = "손맛 점수를 입력해야 합니다.")
    @Min(0)
    @Max(5)
    private Double tasteScore;
    @NotNull(message = "내용을 입력해야 합니다. ")
    @Size(min=5,max=1000,message = "내용은 5~1000자 이어야합니다.")
    private String content;
    @Size(min=0,max=20,message = "가능한 사진 개수는 0~20개 입니다.")
    private Long[] fileList;
}