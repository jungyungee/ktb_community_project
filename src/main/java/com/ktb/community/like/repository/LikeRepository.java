package com.ktb.community.like.repository;

import com.ktb.community.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // user id와 post id로 찾는 단순한 조회이므로
    // 쿼리 메소드 사용
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    // 좋아요 토글 기능을 위한 쿼리 메소드
    // 현재 좋아요 여부 확인
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
