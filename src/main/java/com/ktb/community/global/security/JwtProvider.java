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

    // 30분 유효기간 (액세스 토큰용)
    private final long accessTokenValidity = 1000L * 60 * 30;
    // 14일 유효기간 (리프레쉬 토큰용)
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 14;

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

    // 리프레쉬 토큰 jwt로 생성
    public String createRefreshToken(Long userId){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime()+refreshTokenValidity);

        return Jwts.builder()
                .subject(String.valueOf(userId)) // 주체
                .issuedAt(now) // 발급 시간
                .expiration(expiryDate) // 만료 시간
                .signWith(getSigningKey()) // 시크릿 키로 서명
                .compact(); //토큰 생성
    }

    // 토큰 검증 (유효한 토큰인지 검사)
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // 시크릿 키로 검증
                    .build() // 파서 생성
                    .parseSignedClaims(token); //토큰 해석, 서명 검증, 만료 시간 검증

            return true;
        } catch (Exception e) {
            // 예외 상황
            return false;
        }
    }

    // 토큰에서 유저 정보 추출
    public Long getUserId(String token){
        String subject = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token) //토큰 검증
                .getPayload()
                .getSubject();

        // String인 유저 아이디를 실제 ID 값으로 변환해서 반환
        return Long.valueOf(subject);
    }
}
