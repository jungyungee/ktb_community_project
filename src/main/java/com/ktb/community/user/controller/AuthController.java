package com.ktb.community.user.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.user.dto.LoginRequest;
import com.ktb.community.user.dto.LoginResponse;
import com.ktb.community.user.service.AuthService;
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
            @RequestBody LoginRequest request
    ){
        LoginResponse response = authService.login(request);
        return new ApiResponse<>("login_success",response);
    }
}
