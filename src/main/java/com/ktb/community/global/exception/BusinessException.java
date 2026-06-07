package com.ktb.community.global.exception;

import lombok.Getter;

@Getter
// ErrorCode를 담아서 던지는 예외 객체
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode){
        // BusinessException은 RuntimeException의 자식으로,
        // 부모클래스의 기능도 같이 쓴다.
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
