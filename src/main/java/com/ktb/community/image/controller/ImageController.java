package com.ktb.community.image.controller;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.image.dto.PresignedUrlRequest;
import com.ktb.community.image.dto.PresignedUrlResponse;
import com.ktb.community.image.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 이미지 파일을 S3에 직접 업로드할 수 있도록 Presigned URL을 발급하는 컨트롤러
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    // 프론트가 전달한 이미지 정보를 검증한 뒤 S3 PUT 업로드용 URL 발급
    @PostMapping("/presigned-url")
    public ApiResponse<PresignedUrlResponse> createPresignedUrl(
            HttpServletRequest servletRequest,
            @Valid @RequestBody PresignedUrlRequest request
    ) {
        // 인증되지 않은 사용자가 S3 업로드 URL을 발급받지 못하도록 확인
        Long userId = (Long) servletRequest.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 실제 파일은 백엔드를 거치지 않고 프론트에서 발급받은 URL로 S3에 직접 업로드
        PresignedUrlResponse response = imageService.createPresignedUrl(request);
        return new ApiResponse<>("presigned_url_created", response);
    }
}
