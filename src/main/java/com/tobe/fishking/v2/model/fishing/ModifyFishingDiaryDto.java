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
    @Size(min=5,max=30,message="제목은 5자~30자 이어야 합니다.")
    private String title;
    @Size(min=1,message = "어종 항목이 비었습니다.")
    private String[] fishingSpecies;
//    @NotNull(message = "날짜 항목이 비었습니다.")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate writeDate;

    private String tide;

    private String[] fishingTechnicList;

    private String[] fishingLureList;

    private String fishingType;
    @NotNull(message = "업체id가 비었습니다. ")
    private Long shipId;
    @NotNull(message = "내용 항목이 비었습니다. ")
    @Size(min=5,max=1000,message="내용은 5자~1000자 이어야 합니다.")
    private String content;
    @Size(min=1,max = 20, message = "사진 항목이 비었습니다.")
    private Long[] fileList;
    private Long videoId;
}
