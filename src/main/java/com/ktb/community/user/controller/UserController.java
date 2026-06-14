package com.ktb.community.user.controller;

import com.ktb.community.global.response.ApiResponse;
import com.ktb.community.user.dto.*;
import com.ktb.community.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController //HTTP 요청을 처리하는 컨트롤러
@RequestMapping("/users")
@RequiredArgsConstructor // 생성자 자동생성 및 UserService 주입
// POST /users 요청을 받는 컨트롤러
public class UserController {
    // 회원가입 요청이 오면, userService를 호출
    // final을 사용해서 입력값을 고정
    private final UserService userService;

    // 클라이언트가 보낸 JSON을 SignupRequest로 받고
    // userService.signup(request)로 호출해서 비즈니스 로직 수행
    // 응답으로 돌아온 SignupResponse 로 응답 보냄
    @PostMapping
    public ApiResponse<SignupResponse> signup(
            @Valid
            @RequestBody SignupRequest request
    ){
        SignupResponse response = userService.signup(request);
        return new ApiResponse<>("user_created", response);
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUser(
            HttpServletRequest servletRequest
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        UserResponse response = userService.getUser(userId);
        return new ApiResponse<>("user_fetched", response);
    }

    // 내 정보 수정
    @PatchMapping("/me")
    public ApiResponse<UserUpdateResponse> updateUser(
            HttpServletRequest servletRequest,
            @RequestBody UserUpdateRequest request
    ){
        Long userId = (Long) servletRequest.getAttribute("userId");
        UserUpdateResponse response = userService.updateUser(userId, request);
        return new ApiResponse<>("user_updated", response);
    }
}
