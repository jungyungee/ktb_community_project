package com.ktb.community.post.dto;

import lombok.Getter;

import java.util.List;

// 게시글 리스트 조회 응답 dto
@Getter
public class PostListResponse {
    private List<PostItemResponse> content;
    private String nextCursor;
    private boolean hasNext;
}
