package com.ktb.community.post.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class PostAuthorResponse {
    private String nickname;
    private String profileImageUrl;
}