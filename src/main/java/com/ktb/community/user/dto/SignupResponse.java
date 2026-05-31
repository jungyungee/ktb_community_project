package com.ktb.community.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 회원가입 성공 시 응답 data를 담는 객체
@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long id;
}
