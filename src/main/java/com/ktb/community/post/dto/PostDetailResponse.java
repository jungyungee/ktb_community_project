package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostDetailResponse {
    private Long id;
    private PostCategoryResponse category;
    private String title;
    private String content;
    private String postImageUrl;
    private LocalDateTime createdAt;

    private PostAuthorResponse author;

    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;

    private Boolean liked; // 로그인 한 사용자 기준으로 좋아요 누른 것인지 아닌지
    private Boolean isOwner; // 로그인 한 사용자 기준으로 해당 사용자가 작성한 것인지
}
