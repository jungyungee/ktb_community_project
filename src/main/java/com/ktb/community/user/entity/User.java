package com.ktb.community.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // JPA가 필요로 하는 기본 생성자(엔티티)
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    @Column(name ="deleted_at")
    private LocalDateTime deletedAt;

    // 회원가입용 생성자 정의
    // @AllArgsConstructor를 쓰지 않는 이유: 해당 엔티티에 있는 모든 필드를 회원가입 로직 시 직접 사용하지 않음 (일부는 DB 생성)
    // 필요한 필드만 받아서 User를 생성하기 위해 생성자 직접 정의
    public User(String email, String password, String nickname, String profileImageUrl){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.status = UserStatus.ACTIVE;
    }
}
