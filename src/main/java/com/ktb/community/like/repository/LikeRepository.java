package com.ktb.community.like.repository;

import com.ktb.community.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // user id와 post id로 찾는 단순한 조회이므로
    // 쿼리 메소드 사용
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    // 좋아요 토글 기능을 위한 쿼리 메소드
    // 현재 좋아요 여부 확인
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    // 게시글 삭제 -> 연관 좋아요 데이터 삭제
    // @Query 사용한 JPQL Bulk delete
    @Modifying
    @Query("""
        DELETE FROM Like l
        WHERE l.post.id = :postId
    """)
    int deleteByPostId(@Param("postId") Long postId);
}
