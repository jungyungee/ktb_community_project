package com.ktb.community.global.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class VisitorCookieManager {
    private static final String VISITOR_ID_COOKIE_NAME = "visitorId";
    private static final int VISITOR_ID_COOKIE_MAX_AGE =
            60 * 60 * 24 * 365;

    // visitor 쿠키 조회 메서드 (비회원 방문 기록 판정을 위해)
    public String findVisitorId(HttpServletRequest servletRequest) {
        Cookie[] cookies = servletRequest.getCookies();

        if (cookies == null) {
            return null; // 쿠키가 없다면 발급 메서드로 갈 수 있도록 함
        }

        for (Cookie cookie : cookies) {
            if (VISITOR_ID_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    // visitor 쿠키 발급 메서드
    public void addVisitorIdCookie(
            HttpServletResponse servletResponse,
            String visitorId
    ) {
        Cookie cookie = new Cookie(
                VISITOR_ID_COOKIE_NAME,
                visitorId
        );

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(VISITOR_ID_COOKIE_MAX_AGE);

        servletResponse.addCookie(cookie);
    }
}
