package com.ktb.community.post.repository;

import com.ktb.community.post.entity.PostViewHistory;
import com.ktb.community.post.entity.ViewerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PostViewHistoryRepository extends JpaRepository<PostViewHistory, Long> {

    // post_view_history 테이블에
    // 저장되어 있던 마지막 조회 시간이
    // 현재 시간 - 1분보다 작거나 같으면
    // 1분 내 조회이므로 UPDATE 하지 못한다
    @Modifying
    @Query("""
        UPDATE PostViewHistory pvh
        SET pvh.lastViewedAt = :now
        WHERE pvh.post.id = :postId
            AND pvh.viewerType = :viewerType
            AND pvh.viewerId = :viewwerId
            AND pvh.lastViewedAt <= :threshold
    """)

    // threshold 값을 service에서 현재시간-1로 정해서 줌
    int updateLastViewedAtIfExpired(
            @Param("postId") Long postId,
            @Param("viewerType") ViewerType viewerType,
            @Param("viewerId") String viewerId,
            @Param("now")LocalDateTime now,
            @Param("threshold") LocalDateTime threshold
    );
}
