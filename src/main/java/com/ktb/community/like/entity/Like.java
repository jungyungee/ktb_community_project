package com.ktb.community.like.entity;

import com.ktb.community.user.entity.User;
import com.ktb.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자와 게시글의 좋아요 상태 저장을 위한 테이블 엔티티
// 유저와 게시글을 FK로, 유니크 제약 조건으로 두고 있다.
// 유나크 제약 조건을 두개의 칼럼을 묶어서 둠으로써,
// 같은 유저가 같은 게시물에 좋아요를 중복으로 할 수 없다.
@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 객체 전체를 불필요하게 조회하지 않도록 LAZY 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
