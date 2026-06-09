package com.ktb.community.post.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String postImageUrl;
    private LocalDateTime createdAt;

    private PostAuthorResponse author;

    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;

    private Boolean liked;
    private Boolean isOwner;
}
