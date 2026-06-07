package com.ktb.community.post.dto;

import java.time.LocalDateTime;

public class PostItemResponse {
    private Long id;
    private String title;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;
    private PostAuthorResponse author;
}
