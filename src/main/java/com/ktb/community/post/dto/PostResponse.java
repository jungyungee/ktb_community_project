package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 게시글 작성 성공 시 응답 data를 담는 객체
@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private PostCategoryResponse category;
    private String title;
    private String content;
    private String postImageUrl;
    private LocalDateTime createdAt;
}
