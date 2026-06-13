package com.ktb.community.post.repository;

import com.ktb.community.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom{
    // findById, save는 기본 제공
}
