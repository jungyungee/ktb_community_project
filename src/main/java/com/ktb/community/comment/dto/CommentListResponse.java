package com.ktb.community.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentListResponse {
    private List<CommentItemResponse> content;
    private String nextCursor;
    private boolean hasNext;
}
