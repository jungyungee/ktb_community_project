package com.ktb.community.image.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.image.dto.PresignedUrlRequest;
import com.ktb.community.image.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

// 이미지 요청을 검증하고 S3 PUT 업로드용 Presigned URL을 생성하는 서비스
@Service
@RequiredArgsConstructor
public class ImageService {
    // 허용할 MIME 타입과 실제 S3 객체 키에 사용할 확장자 매핑
    private static final Map<String, String> ALLOWED_CONTENT_TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif"
    );

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.s3.presigned-url-expiration-seconds}")
    private long expirationSeconds;

    public PresignedUrlResponse createPresignedUrl(PresignedUrlRequest request) {
        // 요청 MIME 타입을 신뢰하지 않고 서버에서 허용한 이미지 형식인지 확인
        String extension = ALLOWED_CONTENT_TYPES.get(request.getContentType());
        if (extension == null) {
            throw new BusinessException(ErrorCode.INVALID_IMAGE_TYPE);
        }

        // 파일명 충돌과 경로 조작을 방지하기 위해 서버에서 UUID 기반 객체 키 생성
        String objectKey = request.getImageType().getDirectory()
                + "/" + UUID.randomUUID() + "." + extension;

        // URL 발급 시 지정한 Content-Type을 실제 S3 PUT 요청에도 동일하게 사용해야 함
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(request.getContentType())
                .build();

        // 설정된 만료 시간 동안에만 사용할 수 있는 업로드 요청 생성
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirationSeconds))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        // Presigned URL은 만료되므로 DB에는 쿼리 파라미터가 없는 영구 이미지 URL을 저장
        String imageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + objectKey;

        return new PresignedUrlResponse(
                presignedRequest.url().toString(),
                objectKey,
                imageUrl,
                request.getContentType(),
                expirationSeconds
        );
    }
}
