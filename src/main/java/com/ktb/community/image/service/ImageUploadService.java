package com.ktb.community.image.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.image.dto.ImageType;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    // presigned url 발급
    public PresignedUrlResponse createPresignedUrl(PresignedUrlRequest request) {
        validateContentType(request.getContentType());

        String objectKey = createObjectKey(request.getType(), request.getContentType());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(request.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest =
                s3Presigner.presignPutObject(presignRequest);

        String uploadUrl = presignedRequest.url().toString();
        String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + objectKey;

        return new PresignedUrlResponse(uploadUrl, fileUrl);
    }

    // 이미지 형식 검증
    private void validateContentType(String contentType){
        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")){
            throw new BusinessException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    // S3 저장될 파일 경로 만들기
    private String createObjectKey(ImageType type, String contentType) {

        // 이미지 용도에 따라 저장할 디렉터리 결정
        // PROFILE_IMAGE -> profile-images/
        // POST_IMAGE -> post-images/
        String directory = switch (type) {
            case PROFILE_IMAGE -> "profile-images";
            case POST_IMAGE -> "post-images";
        };

        // Content-Type을 이용하여 파일 확장자 결정
        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            default -> throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
        };

        // UUID -> 중복되지 않는 파일명을 생성
        return directory + "/" + UUID.randomUUID() + extension;
    }
}
