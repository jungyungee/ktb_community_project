package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostItemResponse {
    private Long id;
    private String title;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;
    private PostAuthorResponse author;
}
