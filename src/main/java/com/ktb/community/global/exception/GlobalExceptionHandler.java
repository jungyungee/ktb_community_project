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
    // 이메일 중복 예외 처리를 위한 메소드
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>("email_already_exists", null));
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleNicknameAlreadyExists(){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>("nickname_already_exists", null));
    }
}
