package com.ktb.community.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// 유저 엔티티 (DB에 만든 유저 테이블을 객체로)
// 상태를 나타내는 status는 UserStatus enum 타입으로 정의
@Entity
// 해당 엔티티는 users 테이블과 연결
@Table(name = "users")
public class User {
    //PK (AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;
}
