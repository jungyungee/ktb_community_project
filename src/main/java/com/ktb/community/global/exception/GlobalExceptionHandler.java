package com.ktb.community.global.exception;

import com.ktb.community.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 프로젝트 내 예외들을 처리
// 각 예외 상황에 맞는 반환 값 정의
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException e
    ) {

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        new ApiResponse<>(
                                errorCode.getMessage(),
                                null
                        )
                );
    }
}