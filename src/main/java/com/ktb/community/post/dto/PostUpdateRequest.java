package com.ktb.community.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class PostUpdateRequest {
    private String title;
    private String content;

    // 이미지가 null로 오면 이미지 수정 X (원래 이미지 유지)
    private String postImageUrl;

    // 이미지 삭제를 위한 boolean 값 설정 - 해당 값이 true로 오면 삭제 (이미지가 null 이어도)
    private Boolean deleteImage;
}
