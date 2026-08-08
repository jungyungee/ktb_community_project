package com.ktb.community.post.repository;

import com.ktb.community.post.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 게시글 카테고리 조회 및 저장을 위한 Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    // 클라이언트가 전달한 코드에 해당하는 카테고리 조회
    Optional<PostCategory> findByCode(String code);

    // 프론트 카테고리 목록 표시를 위해 등록 순서대로 조회
    List<PostCategory> findAllByOrderByIdAsc();
}
