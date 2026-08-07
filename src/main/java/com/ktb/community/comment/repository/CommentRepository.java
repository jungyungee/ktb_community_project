package com.ktb.community.comment.repository;

import com.ktb.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
    // 게시글 삭제 -> 해당 게시글에 연관된 댓글 삭제
    // @Query 사용한 JPQL Bulk delete
    // 삭제 쿼리 실행 전 변경사항이 있다면 먼저 DB에 반영하기 위해 flushAutomatically 사용
    @Modifying(flushAutomatically = true)
    @Query("""
        DELETE FROM Comment c
        WHERE c.post.id = :postId
    """)
    int deleteByPostId(@Param("postId") Long postId);

    // 수정을 위한 댓글 조회
    // commentId + userId 조건으로
    @Query("""
        SELECT c
        FROM Comment c
        WHERE c.id = :commentId
          AND c.user.id = :userId
    """)
    Optional<Comment> findByIdAndUserId(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId
    );

    // 삭제를 위한 댓글 조회
    // commentId + userId + post 까지
    @Query("""
        SELECT c
        FROM Comment c
        JOIN FETCH c.post
        WHERE c.id = :commentId
          AND c.user.id = :userId
    """)
    Optional<Comment> findByIdAndUserIdWithPost(
            @Param("commentId") Long commentId,
            @Param("userId") Long userId
    );
}
