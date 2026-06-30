package com.ktb.community.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlResponse {
    // 업로드용 presignedUrl
    private String uploadUrl;
    // 실제 이미지 저장될 주소
    private String fileUrl;
}
