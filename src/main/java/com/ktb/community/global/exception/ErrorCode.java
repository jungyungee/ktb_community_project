package com.ktb.community.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
// 상태코드와 메세지
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "email_already_exists"
    ),

    NICKNAME_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "nickname_already_exists"
    ),

    INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "invalid_request"
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "internal_server_error"
    ),
    INVALID_EMAIL_OR_PASSWORD(
            HttpStatus.UNAUTHORIZED,
            "invalid_email_or_password"
    );

    // 필드
    private final HttpStatus status;
    private final String message;
}
