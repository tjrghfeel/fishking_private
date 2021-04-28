package com.tobe.fishking.v2.model.common;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriteReviewDto {
    @NotNull
    private Long goodsId;
    @NotNull
    @Pattern(regexp = "^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]$",message = "날짜 형식이 맞지 않습니다.")
    private String fishingDate;
    @NotNull(message = "청결도 점수를 입력해야 합니다. ")
    @Min(value = 0, message = "점수값은 0~5점입니다")
    @Max(value = 5, message = "점수값은 0~5점입니다")
    private Double cleanScore;
    @NotNull(message = "서비스 점수를 입력해야 합니다.")
    @Min(value = 0, message = "점수값은 0~5점입니다")
    @Max(value = 5, message = "점수값은 0~5점입니다")
    private Double serviceScore;
    @NotNull(message = "손맛 점수를 입력해야 합니다.")
    @Min(value = 0, message = "점수값은 0~5점입니다")
    @Max(value = 5, message = "점수값은 0~5점입니다")
    private Double tasteScore;
    @NotNull(message = "내용을 입력해야 합니다. ")
    @Size(min=1, message = "내용을 입력해야합니다")
    @Size(max=2000, message = "내용은 2000자 이하이어야합니다")
    private String content;
    @Size(min=0,max=20,message = "가능한 사진 개수는 0~20개 입니다.")
    private Long[] fileList;
}
