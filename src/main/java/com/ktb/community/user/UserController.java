package com.ktb.community.user;

import com.ktb.community.user.dto.SignupRequest;
import com.ktb.community.user.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //HTTP 요청을 처리하는 컨트롤러
@RequestMapping("/users") //
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
    public SignupResponse signup(
            @RequestBody SignupRequest request
    ) {
        return userService.signup(request);
    }
}
