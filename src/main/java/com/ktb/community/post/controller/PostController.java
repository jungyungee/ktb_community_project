package com.ktb.community.post.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.post.dto.*;
import com.ktb.community.post.entity.ViewerType;
import com.ktb.community.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
            @Valid @RequestBody PostRequest request,
            HttpServletRequest servletRequest
            ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        PostResponse response = postService.createPost(userId, request);
        return new ApiResponse<>("post_created", response);
    }

    // 클라이언트의 게시물 리스트 조회 요청과 uri로 들어온 cursor 값으로
    // PostService의 메서드를 호출해서 비즈니스 로직 수행 (커서 값에 따라서 게시물 리스트 가져오기)
    // 조회 완료 시, 해당하는 응답 (정렬된 게시물들 10개) 내보냄
    @GetMapping
    public ApiResponse<PostListResponse> getPostList(
            // uri 의 파라미터로 받은 커서 값 String cursor에 저장 (디코딩은 서비스 로직에서)
            @RequestParam(required = false) String cursor
    ){
        PostListResponse response = postService.getPostList(cursor);
        return new ApiResponse<>("post_list_fetched", response);
    }

    // 게시물 단건 상세조회
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPost(
            // 게시글 id를 전달해야함
            @PathVariable Long postId,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        // 비회원 조회도 허용함에 따라서 타입에 따라 다른 처리 필요
        ViewerType viewerType;
        String viewerId;

        if (userId == null){
            // 로그인한 회원
            viewerType = ViewerType.USER;
            viewerId = String.valueOf(userId);
        } else {
            // 로그인하지 않은 조회 = 비회원 간주
            viewerType = ViewerType.VISITOR;
            viewerId = findVisitorId(servletRequest); // 비회원의 경우 쿠키에 이전 조회 내역이 있는지

            // 쿠키에 이전 조회 내역이 없다면
            if (viewerId == null){
                viewerId = UUID.randomUUID().toString();
                addVisitorIdCookie(servletResponse, viewerId);
            }
        }

        PostDetailResponse response = postService.getPost(userId, postId, viewerType, viewerId);
        return new ApiResponse<>("post_fetched", response);
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ApiResponse<PostUpdateResponse> updatePost(
            @RequestBody PostUpdateRequest request,
            @PathVariable Long postId,
            HttpServletRequest servletRequest
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        PostUpdateResponse response = postService.updatePost(userId, postId, request);
        return new ApiResponse<>("post_updated", response);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    // 따로 응답 타입이 없으므로 response status 붙임
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long postId,
            HttpServletRequest servletRequest
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        postService.deletePost(userId, postId);
    }
}
