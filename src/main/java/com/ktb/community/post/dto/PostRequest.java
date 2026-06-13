package com.ktb.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 게시글 작성 요청 JSON을 담는 객체
@Getter
public class PostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String postImageUrl;
}
