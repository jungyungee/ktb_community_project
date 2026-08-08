package com.ktb.community.post.repository;

import com.ktb.community.post.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByCode(String code);

    List<PostCategory> findAllByOrderByIdAsc();
}
