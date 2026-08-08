package com.ktb.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 게시글 작성 요청 JSON을 담는 객체
@Getter
public class PostRequest {
    // INFO, REVIEW, QNA, COMPANY 중 선택한 게시판 코드
    @NotBlank
    private String categoryCode;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String postImageUrl;
}
