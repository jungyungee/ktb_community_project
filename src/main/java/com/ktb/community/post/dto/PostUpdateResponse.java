package com.ktb.community.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostUpdateResponse {
    private Long id;
    private String title;
    private String content;
    private String postImageUrl;
    private LocalDateTime updatedAt;
}
