package com.ktb.community.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private Long postId;
    private int likeCount;
    private boolean liked;
}
