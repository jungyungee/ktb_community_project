package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 게시물 리스트 내 content에 들어갈 dto (게시글 본문 필요없음)
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
