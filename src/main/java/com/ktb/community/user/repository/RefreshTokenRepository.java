package com.ktb.community.user.repository;

import com.ktb.community.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // 쿠키로 들어온 토큰 디비에 존재하는지 조회
    Optional<RefreshToken> findByToken(String token);
    // 토큰 제거 메서드
    void deleteByUserId(Long userId);
}