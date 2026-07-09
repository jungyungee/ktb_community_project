package com.ktb.community.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 요청 들어올 때 Authorization 헤더 확인하는 클래스
// 스프링 필터 클래스 상속 (한 요청 당 한번만 실행되는 필터)
// 스프링이 이 클래스를 Bean으로 등록하고, JwtProvider를 생성자로 주입해줌
@Component
@RequiredArgsConstructor // 생성자 자동 생성
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/server-api/health");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더의 토큰 꺼내는 작업
        String authorizationHeader = request.getHeader("Authorization");

        // bearer 자르고 순수 토큰만 accessToken 에 저장
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring(7);

            // 추출한 accessToken 이용해서 검증, id 추출 수행
            if (jwtProvider.validateToken(accessToken)){
                Long userId = jwtProvider.getUserId(accessToken);
                // 이 요청 안에 userId를 저장해두어서 컨트롤러에서 꺼낼 수 있도록
                request.setAttribute("userId", userId);
            }
        }

        // 필터를 끝내고 다음 단계 (컨트롤러)로 넘김
        filterChain.doFilter(request,response);
    }

}
