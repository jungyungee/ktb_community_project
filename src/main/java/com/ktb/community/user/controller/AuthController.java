package com.ktb.community.user.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.user.dto.LoginRequest;
import com.ktb.community.user.dto.LoginResponse;
import com.ktb.community.user.dto.LoginResult;
import com.ktb.community.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor // 생성자 자동생성 및 UserService 주입
// POST /auth 요청을 받는 컨트롤러
public class AuthController {
    // 로그인 요청이 오면, authService를 호출
    // final로 입력값을 고정
    private final AuthService authService;

    // 클라이언트가 보낸 JSON을 LoginRequest로 받고
    // authService.login(request)로 호출해서 비즈니스 로직 수행
    // 응답으로 돌아온 LoginResponse 로 응답 보냄
    @PostMapping
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse servletResponse
    ){
        // 로그인 결과 - 액세스, 리프레쉬 토큰 포함
        LoginResult result = authService.login(request);

        // 쿠키 생성 (브라우저에게 보낼)
        Cookie refreshCookie = new Cookie(
            "refreshToken",
                result.getRefreshToken()
        );

        refreshCookie.setHttpOnly(true); //document.cookie 로 읽을 수 없도록 보안을 위해 추가
        refreshCookie.setPath("/"); // API 모든 경로에서 쿠키 전송 가능하도록
        refreshCookie.setMaxAge(60 * 60 * 24 * 14); // 쿠키 만료 시간
        // refreshCookie.setSecure(true); //HTTPS 배포 시 추가

        // 생성한 쿠키를 HTTP 응답 헤더에 추가
        servletResponse.addCookie(refreshCookie);

        return new ApiResponse<>("login_success", new LoginResponse(result.getAccessToken()));
    }
}
