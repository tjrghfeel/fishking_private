package com.tobe.fishking.v2.model.common;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long eventId;
    private Long shipId;
    private String eventTitle;
    private String createdDate;
    private ArrayList<String> imageUrlList;
    private String content;
    private int likeCount;
    private int commentCount;
    private Boolean isLikeTo;

    private Boolean isActive;
    private String startDate;
    private String endDate;
    private ArrayList<Long> fileIdList;
}
