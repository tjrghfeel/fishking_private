package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFishingDiary {
    @NotNull
    private Long fishingDiaryId;
}
