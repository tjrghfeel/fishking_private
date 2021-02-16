package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingDiaryCommentPageDto {
    private int commentCount;
    private String fishingDiaryTitle;
    private List<FishingDiaryCommentDtoForPage> commentList;
}
