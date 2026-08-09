package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 게시글 카테고리 코드와 화면 표시 이름을 전달하는 응답 DTO
public class PostCategoryResponse {
    private String code;
    private String name;
}
