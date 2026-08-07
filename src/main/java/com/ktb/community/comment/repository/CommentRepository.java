package com.ktb.community.comment.repository;

import com.ktb.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
    @Modifying
    @Query("""
        DELETE FROM Comment c
        WHERE c.post.id = :postId
    """)
    int deleteByPostId(@Param("postId") Long postId);
}
