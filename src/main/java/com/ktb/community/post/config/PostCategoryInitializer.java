package com.ktb.community.post.config;

import com.ktb.community.post.entity.PostCategory;
import com.ktb.community.post.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostCategoryInitializer implements CommandLineRunner {
    private final PostCategoryRepository postCategoryRepository;

    @Override
    public void run(String... args) {
        List<PostCategory> categories = List.of(
                new PostCategory("INFO", "정보"),
                new PostCategory("REVIEW", "후기"),
                new PostCategory("QNA", "질문"),
                new PostCategory("COMPANY", "동행")
        );

        categories.stream()
                .filter(category -> postCategoryRepository.findByCode(category.getCode()).isEmpty())
                .forEach(postCategoryRepository::save);
    }
}
