package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 게시물 리스트 내 content에 들어갈 dto (게시글 본문 필요없음)
@Getter
@AllArgsConstructor
public class PostItemResponse {
    private Long id;
    private PostCategoryResponse category;
    private String title;
    // 목록 피드에서도 게시글 대표 이미지를 표시할 수 있도록 이미지 URL 전달
    private String postImageUrl;
    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private PostAuthorResponse author;
}
