package com.ktb.community.post.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.post.dto.PostRequest;
import com.ktb.community.post.dto.PostResponse;
import com.ktb.community.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
// POST /posts 요청을 받는 컨트롤러
public class PostController {
    // 게시글 post 요청이 오면, postService를 호출
    // final 로 입력값 고정
    private final PostService postService;

    // 클라이언트가 보낸 JSON을 PostRequest로 받고
    // postService 의 메서드를 호출해서 비즈니스 로직 수행, 디비에 저장 및 업데이트
    // 응답으로 PostResponse 내보냄
    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @Valid @RequestBody PostRequest request
            ){
        PostResponse response = postService.post(request);
        return new ApiResponse<>("post_created", response);
    }
}
