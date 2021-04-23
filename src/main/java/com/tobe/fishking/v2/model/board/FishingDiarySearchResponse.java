package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FishingDiarySearchResponse {

    private Long id;
    private String title;
    private String contents;
    private String imageUrl;
    private String nickName;
    private String profileImageUrl;
    private String createdDate;
    private Integer loves;
    private Integer comments;
    private String fishingType;
    private String species;

    public FishingDiarySearchResponse(
            Long id,
            String title,
            String contents,
            String imageUrl,
            String nickName,
            String profileImageUrl,
            LocalDateTime createdDate,
            Long loves,
            Long comments,
            FishingType fishingType,
            String species) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.imageUrl = imageUrl.split("\\^")[0];
        this.nickName = nickName;
        this.profileImageUrl = "https://www.fishkingapp.com/resource"+profileImageUrl;
        this.createdDate = DateUtils.getDateInFormat(createdDate.toLocalDate());
        this.loves = loves.intValue();
        this.comments = comments.intValue();
        this.fishingType = fishingType == null ? "" : fishingType.getValue();
        this.species = species;
    }

}
