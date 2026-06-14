package com.ktb.community.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 리프레시 토큰 엔티티
// 로그인 시 발급한 refresh token을 저장하는 테이블
@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 생성자
    public RefreshToken(
            Long userId,
            String token,
            LocalDateTime expiresAt
    ) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }
}
