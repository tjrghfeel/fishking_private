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

    public void plusViewCount(){this.viewCount++;}
    public void plusLikeCount(){this.likeCount++;}
    public void plusCommentCount(){this.commentCount++;}
    public void plusShareCount(){this.shareCount++;}
    public void subLikeCount(){this.likeCount--;}
    public void subCommentCount(){this.commentCount--;}
    public void subShareCount(){this.shareCount--;}
}