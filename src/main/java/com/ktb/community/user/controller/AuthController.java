package com.ktb.community.user.controller;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.user.dto.LoginRequest;
import com.ktb.community.user.dto.LoginResponse;
import com.ktb.community.user.dto.LoginResult;
import com.ktb.community.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    // 브라우저 요청에 담겨온 리프레쉬 토큰을 가지고 액세스 토큰 재발급
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(
            HttpServletRequest servletRequest
    ){
        String refreshToken = null;

        // 요청에 담겨온 현재 쿠키 가져오기
        Cookie[] cookies = servletRequest.getCookies();

        if (cookies != null) {
            // 쿠키 내를 반복문으로 돌면서 이름이 refreshToken인 쿠키 찾기
            for (Cookie cookie: cookies){
                if ("refreshToken".equals(cookie.getName())){
                    // refreshToken인 쿠키에서 값을 가져오기
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // 리프레쉬 토큰이 쿠키에 없는 경우
        if (refreshToken == null){
            throw new BusinessException(
                    ErrorCode.INVALID_REFRESH_TOKEN
            );
        }

        // 리프레쉬 토큰이 쿠키에 있으면 액세스 토큰 재발급해서 응답으로 돌려줌
        LoginResponse response = authService.refresh(refreshToken);
        return new ApiResponse<>("token_refreshed", response);
    }

    // 로그아웃
    // 디비 리프레쉬 토큰 삭제
    // 쿠키 삭제 (만료)
    @PostMapping("/auth/delete")
    public ApiResponse<Void> logout(
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        // 로그아웃 (디비에서 리프레쉬 토큰 삭제)
        authService.logout(userId);

        // 리프레쉬 토큰 쿠키 삭제 (브라우저 상에서)
        // null 쿠키 만들어서 즉시 만료
        Cookie refreshCookie = new Cookie(
                "refreshToken",
                null
        );
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0); // 쿠키 즉시 만료시킴

        // 만료를 보내면 브라우저는 쿠키 삭제
        servletResponse.addCookie(refreshCookie);

        return new ApiResponse<>("logout_success", null);
    }
}
