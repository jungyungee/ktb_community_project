package com.ktb.community.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ktb.community.user.entity.User;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 26)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "post_image_url")
    private String postImageUrl;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "comment_count")
    private int commentCount = 0;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name ="created_at")
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;

    // 게시글 작성용 생성자 정의
    public Post(User user, String title, String content, String postImageUrl) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.postImageUrl = postImageUrl;
    }

    // 게시글 수정
    public void update(String title, String content, String postImageUrl, Boolean deleteImage){
        if(title != null){
            this.title = title;
        }
        if(content != null){
            this.content = content;
        }
        // 이미지 수정하지 않는 것과 이미지 삭제를 구분
        if (Boolean.TRUE.equals(deleteImage)){
            this.postImageUrl = null;
        } else if (postImageUrl != null ){
            this.postImageUrl = postImageUrl;
        }
    }

    // 게시물 작성 DB 생성 시간
    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    // 게시글 좋아요 수 카운팅
    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }
}
