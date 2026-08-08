package com.ktb.community.post.config;

import com.ktb.community.post.entity.PostCategory;
import com.ktb.community.post.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
// 애플리케이션 시작 시 기본 게시글 카테고리를 생성하는 컴포넌트
public class PostCategoryInitializer implements CommandLineRunner {
    private final PostCategoryRepository postCategoryRepository;

    @Override
    public void run(String... args) {
        // 여행 커뮤니티에서 사용하는 게시판 종류
        List<PostCategory> categories = List.of(
                new PostCategory("INFO", "정보"),
                new PostCategory("REVIEW", "후기"),
                new PostCategory("QNA", "질문"),
                new PostCategory("COMPANY", "동행")
        );

        // 이미 저장된 코드는 제외하여 애플리케이션 재시작 시 중복 생성을 방지
        categories.stream()
                .filter(category -> postCategoryRepository.findByCode(category.getCode()).isEmpty())
                .forEach(postCategoryRepository::save);
    }
}
