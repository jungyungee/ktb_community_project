package com.ktb.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 전역 웹 설정
// 현재는 프론트(localhost:3000)와 백엔드(localhost:8080)를
// 서로 통신할 수 있도록 CORS 설정만 담당
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 API 경로에 대해 CORS 허용
        registry.addMapping("/**")
                // 허용할 출처(Origin)
                // 현재는 로컬 개발 환경의 프론트 서버만 허용
                // 배포 시에는 실제 프론트 도메인으로 변경 예정
                .allowedOrigins("http://localhost:3000")

                // 허용할 HTTP 메서드
                // OPTIONS 는 브라우저의 Preflight 요청 처리용
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")

                // 요청 헤더 허용 (현재는 모든 헤더 허용)
                // Authorization 헤더 accessToken (JWT) 사용을 위해 필요
                .allowedHeaders("*")

                // 쿠키 포함 요청 허용
                // refreshToken을 HttpOnly Cookie로 사용하므로 필요
                // 프론트 fetch 요청에서도 credentials: "include" 설정 필요
                .allowCredentials(true);
    }
}