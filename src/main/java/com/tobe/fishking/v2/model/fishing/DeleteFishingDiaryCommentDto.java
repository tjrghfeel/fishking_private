package com.tobe.fishking.v2.model.fishing;

import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFishingDiaryCommentDto {
    @NotNull
    private Long commentId;
}
