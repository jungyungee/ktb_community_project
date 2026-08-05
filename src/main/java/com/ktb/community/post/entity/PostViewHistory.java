package com.ktb.community.post.entity;

import com.ktb.community.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "post_view_history",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {
                "post_id", "viewer_type", "viewer_id"
            }
        )
})

public class PostViewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "viewer_type", nullable = false)
    private ViewerType viewerType;

    @Column(name = "viewer_id", nullable = false, length = 64)
    private String viewerId;

    @Column(name = "last_viewed_at", nullable = false)
    private LocalDateTime lastViewedAt;

    public PostViewHistory(Post post, ViewerType viewerType, String viewerId,  LocalDateTime lastViewedAt) {
        this.post = post;
        this.viewerType = viewerType;
        this.viewerId = viewerId;
        this.lastViewedAt = lastViewedAt;
    }
}
