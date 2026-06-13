package com.ktb.community.comment.cursor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCursor {
    private String createdAt;
    private Long id;
}
