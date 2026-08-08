package com.ktb.community.post.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.post.dto.PostCategoryResponse;
import com.ktb.community.post.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post-categories")
@RequiredArgsConstructor
// 프론트에서 사용할 게시글 카테고리 목록을 제공하는 컨트롤러
public class PostCategoryController {
    private final PostCategoryRepository postCategoryRepository;

    // 등록된 게시글 카테고리를 전체 조회
    @GetMapping
    public ApiResponse<List<PostCategoryResponse>> getCategories() {
        List<PostCategoryResponse> categories = postCategoryRepository.findAllByOrderByIdAsc().stream()
                .map(category -> new PostCategoryResponse(category.getCode(), category.getName()))
                .toList();

        return new ApiResponse<>("post_categories_fetched", categories);
    }
}
