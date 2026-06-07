package com.ktb.community.user.dto;

import lombok.Getter;

// 회원가입 요청 JSON을 담는 객체
@Getter
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;
}
