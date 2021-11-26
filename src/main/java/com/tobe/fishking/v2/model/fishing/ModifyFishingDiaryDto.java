package com.tobe.fishking.v2.model.fishing;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyFishingDiaryDto {
    @NotNull(message = "조항일지 또는 유저조행기 id가 비었습니다.")
    private Long fishingDiaryId;
//    @NotNull(message = "카테고리가 비었습니다.")
//    private String category;
    @NotNull(message = "제목이 비었습니다. ")
    @Size(min=1,message="제목을 입력하세요")
    @Size(max=200, message = "제목은 200자 이하이어야합니다")
    private String title;
    @Size(min=1,message = "어종 항목이 비었습니다.")
    private String[] fishingSpecies;
    @NotNull(message = "날짜 항목이 비었습니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fishingDate;

    private String tide;

    private String[] fishingTechnicList;

    private String[] fishingLureList;

    private String fishingType;
    private Long shipId;
    @NotNull(message = "내용 항목이 비었습니다. ")
    @Size(min=1,message="내용이 비었습니다.")
    @Size(max=1000, message = "내용은 1000자 이하이어야합니다")
    private String content;
    @Size(min=1,max = 25, message = "사진 항목이 비었습니다.")
    private Long[] fileList;
    private Long videoId;

    private String address;
    private Double latitude;
    private Double longitude;
}
