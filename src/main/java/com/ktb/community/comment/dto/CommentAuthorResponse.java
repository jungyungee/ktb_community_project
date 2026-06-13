package com.ktb.community.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentAuthorResponse {
    private String nickname;
    private String profileImageUrl;
}
