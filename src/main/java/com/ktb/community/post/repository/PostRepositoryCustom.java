package com.ktb.community.post.repository;

import com.ktb.community.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    // 커서 size개의 게시글 가지고 오는 메서드
    // 마지막 게시글의 createdAt, id 를 가지고 다음 게시글을 찾을 수 있다.
    // size는 현재는 10으로 결정했지만 확장성을 위해 변수로 둔다.
    List<Post> findPostsByCursor(
            LocalDateTime createdAt,
            Long id,
            int size
    );

    // 조회수 증가 원자적 업데이트 메서드
    long increaseViewCount(Long id);
}
