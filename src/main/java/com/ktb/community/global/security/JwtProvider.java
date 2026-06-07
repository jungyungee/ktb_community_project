package com.ktb.community.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
// JWT 발급/검증을 위한 클래스
public class JwtProvider {
    // 환경변수 값으로 시크릿 코드 주입
    @Value("${jwt.secret}")
    private String secretKey;

    // 30분 유효기간
    private final long accessTokenValidity = 1000 * 60 * 30;

    private SecretKey getSigningKey(){
        // 평문 키를 안전한 SecretKey 객체로 변환
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    // 액세스 토큰 jwt 로 생성
    public String createAccessToken(Long userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+accessTokenValidity);

        return Jwts.builder()
                .subject(String.valueOf(userId)) // 주체
                .issuedAt(now) // 발급 시간
                .expiration(expiryDate) // 만료 시간
                .signWith(getSigningKey()) // 시크릿 키로 서명
                .compact(); //토큰 생성
    }
}
