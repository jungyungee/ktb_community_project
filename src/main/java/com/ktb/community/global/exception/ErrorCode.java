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
    INVALID_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "invalid_refresh_token"
    ),
    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "internal_server_error"
    ),
    INVALID_EMAIL_OR_PASSWORD(
            HttpStatus.UNAUTHORIZED,
            "invalid_email_or_password"
    ),
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "unauthorized"
    ),
    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "user_not_found"
    ),
    INVALID_CURSOR(
            HttpStatus.BAD_REQUEST,
            "invalid_cursor"
    ),
    POST_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "post_not_found"
    ),
    NOT_POST_OWNER(
            HttpStatus.FORBIDDEN,
            "not_post_owner"
    ),
    COMMENT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "comment_not_found"
    ),
    NOT_COMMENT_OWNER(
            HttpStatus.FORBIDDEN,
            "not_comment_owner"
    ),
    USER_ALREADY_DELETED(
            HttpStatus.CONFLICT,
            "user_already_deleted"
    );

    // 필드
    private final HttpStatus status;
    private final String message;
}
