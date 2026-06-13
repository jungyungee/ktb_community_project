package com.ktb.community.comment.entity;

import com.ktb.community.post.entity.Post;
import com.ktb.community.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 객체 전체를 불필요하게 조회하지 않도록 LAZY 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


    // 댓글 추가용 생성자 정의
    public Comment(User user, Post post, String content){
        this.user = user;
        this.post = post;
        this.content = content;
    }

    // 댓글 수정
    public void update(String content){
        if(content != null){
            this.content = content;
        }
    }
}
