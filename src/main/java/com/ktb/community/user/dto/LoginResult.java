package com.ktb.community.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 로그인 내부용 dto (액세스 토큰, 리프레쉬 토큰 포함)
// 서비스 -> 컨트롤러에서만 사용
@Getter
@AllArgsConstructor
public class LoginResult {
    private String accessToken;
    private String refreshToken;
}
