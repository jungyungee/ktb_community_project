package com.ktb.community.like.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.like.dto.LikeResponse;
import com.ktb.community.like.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 좋아요 토글 (좋아요 추가/취소)
    @PostMapping("/{postId}/likes")
    public ApiResponse<LikeResponse> likeToggle(
            HttpServletRequest servletRequest,
            @PathVariable Long postId
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        LikeResponse response = likeService.toggleLike(userId, postId);
        return new ApiResponse<>("like_toggle_pressed", response);
    }
}
