package com.ktb.community.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 공통응답 타입을 위한 리스폰스 타입 (메세지, 데이터 형식으로 공통되게 주기 위해)
public class ApiResponse<T> {
    private String message;
    private T data; //다양한 응답타입을 받기위한 제네릭 타입사용
}
