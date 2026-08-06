package com.ktb.community.post.service;

import com.ktb.community.post.entity.PostViewHistory;
import com.ktb.community.post.entity.ViewerType;
import com.ktb.community.post.repository.PostViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostViewHistoryService {
    private final PostViewHistoryRepository postViewHistoryRepository;

    // 조회수 로직 구현
    public boolean viewCounter(Long postId, ViewerType viewerType, String viewerId) {
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        LocalDateTime threshold = now.minusMinutes(1); // 1분 내 조회인지 판별을 위해 (현재시간-1을 넘김)

        // 기존 기록 중, 1분 이상 전 조회이면 이를 갱신 (성공 시 1 반환)
        int updatedRows =
                postViewHistoryRepository.updateLastViewedAtIfExpired(
                        postId,
                        viewerType,
                        viewerId,
                        now,
                        threshold
                );
        // 갱신 성공 -> 조회수 증가
        if (updatedRows == 1) {
            return true;
        }

        // 갱신 실패 -> 최초 조회인지를 확인하기 위해 INSERT 시도
        int insertedRows =
                postViewHistoryRepository.insertIfAbsent(
                        postId,
                        viewerType.name(),
                        viewerId,
                        now
                );

        // INSERT 성공 시 -> 조회 수 증가
        return insertedRows == 1;
    };
}
