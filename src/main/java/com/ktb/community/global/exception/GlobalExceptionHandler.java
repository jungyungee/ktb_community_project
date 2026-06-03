package com.ktb.community.global.exception;

import com.ktb.community.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 서비스, 컨트롤러에서 예외를 던지면
// 잡아서 정해진 JSON 응답 내려주는 역할
@RestControllerAdvice // throw new BusinessException 의 예외를 잡음
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException e
    ) {
        // BusinessException 객체 e에서 getErrorCode로 에러 코드를 꺼내고
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus()) // 에러 상태 코드
                .body(
                        new ApiResponse<>(
                                errorCode.getMessage(), // 에러 상태 메시지
                                null
                        )
                );
    }
}