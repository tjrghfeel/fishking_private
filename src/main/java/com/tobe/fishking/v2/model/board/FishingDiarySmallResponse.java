package com.tobe.fishking.v2.model.board;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FishingDiarySmallResponse {

    private Long id;
    private String nickName;
    private String profileImage;
    private String title;
    private List<String> imageUrlList;
    private Integer imageCount;
    private String content;
    private String createdAt;

    public FishingDiarySmallResponse(FishingDiary diary, List<String> images, Integer imgCount) {
        this.id = diary.getId();
        this.nickName = diary.createdBy.getNickName();
        this.profileImage = "/resource" + diary.createdBy.getProfileImage();
        this.title = diary.getTitle();
        this.imageUrlList = images;
        this.imageCount = imgCount;
        this.content = diary.getContents();
        this.createdAt = DateUtils.getDateTimeInFormat(diary.getCreatedDate());
    }
}
