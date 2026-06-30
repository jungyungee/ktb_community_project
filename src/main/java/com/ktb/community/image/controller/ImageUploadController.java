package com.ktb.community.image.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.image.dto.PresignedUrlRequest;
import com.ktb.community.image.dto.PresignedUrlResponse;
import com.ktb.community.image.service.ImageUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("imageurls")
public class ImageUploadController {
    private final ImageUploadService imageUploadService;

    @PostMapping
    public ApiResponse<PresignedUrlResponse> createPresignedUrl(
            @Valid @RequestBody PresignedUrlRequest request
    ){
        PresignedUrlResponse response = imageUploadService.createPresignedUrl(request);
        return new ApiResponse<>("presigned_url_created", response);
    }
}
