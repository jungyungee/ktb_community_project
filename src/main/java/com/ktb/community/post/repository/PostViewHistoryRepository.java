package com.ktb.community.post.repository;

import com.ktb.community.post.entity.PostViewHistory;
import com.ktb.community.post.entity.ViewerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PostViewHistoryRepository extends JpaRepository<PostViewHistory, Long> {

    // 조회수 관련 로직들

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
            AND pvh.viewerId = :viewerId
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


    // 최초 조회의 경우 INSERT 하는 로직
    // INSERT 성공 시 1 반환
    // INSERT 실패 시 0 반환
    @Modifying
    @Query(
            value = """
            INSERT IGNORE INTO post_view_history (
                post_id,
                viewer_type,
                viewer_id,
                last_viewed_at
            )
            VALUES (
                :postId,
                :viewerType,
                :viewerId,
                :now
            )
            """,
            nativeQuery = true
    )

    int insertIfAbsent(
            @Param("postId") Long postId,
            @Param("viewerType") String viewerType,
            @Param("viewerId") String viewerId,
            @Param("now") LocalDateTime now
    );

    // 게시글 삭제 -> 해당 게시글과 관련된 조회 기록 데이터 삭제
    // @Query 사용한 JPQL Bulk delete
    @Modifying(flushAutomatically = true)
    @Query("""
        DELETE FROM PostViewHistory pvh
        WHERE pvh.post.id = :postId
    """)
    int deleteByPostId(@Param("postId") Long postId);
}
