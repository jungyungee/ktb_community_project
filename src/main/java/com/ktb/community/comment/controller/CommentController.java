package com.ktb.community.comment.controller;

import com.ktb.community.comment.dto.CommentRequest;
import com.ktb.community.comment.dto.CommentResponse;
import com.ktb.community.comment.service.CommentService;
import com.ktb.community.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 추가
    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request,
            @PathVariable Long postId,
            HttpServletRequest servletRequest
            ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        CommentResponse response = commentService.createComment(userId, postId, request);
        return new ApiResponse<>("comment_created", response);
    }
}
