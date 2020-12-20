package com.tobe.fishking.v2.model.common;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class ShareStatus {

    private int viewCount;
    private int likeCount;
    private int commentCount;
    private int shareCount;

    @Builder
    public ShareStatus(int viewCount, int likeCount, int commentCount, int shareCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
    }
}