package com.ktb.community.image.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

// Presigned URL 발급에 필요한 이미지 정보를 전달받는 요청 DTO
@Getter
public class PresignedUrlRequest {
    // POST 또는 PROFILE 중 이미지가 사용될 위치
    @NotNull
    private ImageType imageType;

    // S3 업로드 요청의 Content-Type과 동일하게 사용할 MIME 타입
    @NotBlank
    private String contentType;

    // 0보다 크고 최대 10MB 이하인 파일만 URL 발급 허용
    @NotNull
    @Positive
    @Max(10 * 1024 * 1024)
    private Long fileSize;
}
