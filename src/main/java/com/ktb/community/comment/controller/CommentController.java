package com.ktb.community.comment.controller;

import com.ktb.community.comment.dto.CommentListResponse;
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

    // 댓글 리스트 조회
    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<CommentListResponse> getCommentList(
            // uri 의 파라미터로 받은 커서 값 String cursor에 저장 (디코딩은 서비스 로직에서)
            @RequestParam(required = false) String cursor,
            @PathVariable Long postId,
            HttpServletRequest servletRequest
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        CommentListResponse response = commentService.getCommentList(cursor, postId, userId);
        return new ApiResponse<>("comment_list_fetched", response);
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @Valid @RequestBody CommentRequest request,
            @PathVariable Long commentId,
            HttpServletRequest servletRequest
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        CommentResponse response = commentService.updateComment(userId, commentId, request);
        return new ApiResponse<>("comment_updated", response);
    }
}
