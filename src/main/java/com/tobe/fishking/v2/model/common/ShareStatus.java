package com.tobe.fishking.v2.model.common;

import javax.persistence.Embeddable;

@Embeddable
public class ShareStatus {
    private int viewCount;
    private int shareCount;
    private int likeCount;
    private int commentCount;
}