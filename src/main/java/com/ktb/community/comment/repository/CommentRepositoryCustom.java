package com.ktb.community.comment.repository;

import com.ktb.community.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepositoryCustom {
    // 커서 size 개의 댓글 가지고 오는 메서드
    // 마지막 댓글의 creatAt, id를 가지고 다음 댓글 찾을 수 있음
    List<Comment> findCommentsByCursor(
            Long postId,
            LocalDateTime createdAt,
            Long id,
            int size
    );
}
