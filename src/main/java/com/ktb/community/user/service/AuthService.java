package com.ktb.community.user.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.global.security.JwtProvider;
import com.ktb.community.global.security.PasswordHash;
import com.ktb.community.user.entity.RefreshToken;
import com.ktb.community.user.entity.UserStatus;
import com.ktb.community.user.repository.RefreshTokenRepository;
import com.ktb.community.user.repository.UserRepository;
import com.ktb.community.user.dto.LoginRequest;
import com.ktb.community.user.dto.LoginResponse;
import com.ktb.community.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// 로그인 로그아웃 관련 비즈니스 로직
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository; //이메일로 조회
    private final RefreshTokenRepository refreshTokenRepository; //리프레쉬 토큰 발급
    private final PasswordHash passwordHash; // 비밀번호 일치 확인
    private final JwtProvider jwtProvider; //jwt 토큰 관리

    // LoginRequest를 받고 email로 존재 여부를 확인
    // email이 존재한다면 비밀번호 검증 로직을 거친 후
    // jwt 발급 시킴
    // 이후 응답객체 dto를 통해 반환
    public LoginResponse login(LoginRequest request){
        // 유저를 찾는다.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        // 해당 이메일 존재하지 않을 경우
                        // 에러 처리를 한다.
                        () -> new BusinessException(ErrorCode.INVALID_EMAIL_OR_PASSWORD)
                );

        // 탈퇴한 유저는 로그인 불가하도록 에러 처리
        if (user.getStatus() == UserStatus.DELETED) {
            throw new BusinessException(ErrorCode.USER_ALREADY_DELETED);
        }

        if (!passwordHash.matches(request.getPassword(),user.getPassword())){
            // 비밀번호가 일치하지 않을 경우
            // 에러 처리를 한다.
            throw new BusinessException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
        // 이메일이 존재하고, 비밀번호가 일치하므로
        // userId 를 이용해 jwt 발급을 시킨다.
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // 기존 리프레쉬 토큰이 있을 경우 1개만 유지하기 위해 삭제
        refreshTokenRepository.deleteByUserId(user.getId());

        // 새 리프레쉬 토큰을 디비에 저장 (응답으로 보내지 않음)
        refreshTokenRepository.save(
                new RefreshToken(
                        user.getId(),
                        refreshToken,
                        LocalDateTime.now().plusDays(14)
                )
        );

        return new LoginResponse(accessToken);
    }
}
