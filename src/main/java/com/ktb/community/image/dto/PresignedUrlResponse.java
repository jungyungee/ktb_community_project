package com.ktb.community.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 프론트의 S3 업로드와 이후 이미지 정보 저장에 필요한 응답 DTO
@Getter
@AllArgsConstructor
public class PresignedUrlResponse {
    // 프론트에서 PUT 요청으로 파일을 업로드할 일회성 URL
    private String uploadUrl;
    // S3에서 이미지를 식별하는 영구 객체 키
    private String objectKey;
    // 게시글 또는 회원 정보에 저장할 이미지 접근 URL
    private String imageUrl;
    // S3 PUT 요청 시 반드시 동일하게 전달해야 하는 Content-Type
    private String contentType;
    // Presigned URL이 유효한 시간(초)
    private long expiresInSeconds;
}
