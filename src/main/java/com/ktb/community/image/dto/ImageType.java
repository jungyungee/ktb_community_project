package com.ktb.community.image.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 이미지 용도에 따라 S3 내부 저장 경로를 구분
@Getter
@RequiredArgsConstructor
public enum ImageType {
    POST("posts"),
    PROFILE("profiles");

    // S3 객체 키의 최상위 디렉터리 이름
    private final String directory;
}
