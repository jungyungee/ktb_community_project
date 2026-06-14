package com.ktb.community.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentItemResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private CommentAuthorResponse author;
    private boolean isOwner;
}
