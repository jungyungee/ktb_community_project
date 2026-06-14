package com.ktb.community.user.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.global.security.JwtProvider;
import com.ktb.community.global.security.PasswordHash;
import com.ktb.community.user.dto.LoginResult;
import com.ktb.community.user.entity.RefreshToken;
import com.ktb.community.user.entity.UserStatus;
import com.ktb.community.user.repository.RefreshTokenRepository;
import com.ktb.community.user.repository.UserRepository;
import com.ktb.community.user.dto.LoginRequest;
import com.ktb.community.user.dto.LoginResponse;
import com.ktb.community.user.entity.User;
import jakarta.transaction.Transactional;
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
    @Transactional // 토큰 삭제 쿼리 - 하나의 트랜잭션 안에서 수행되어야 함
    public LoginResult login(LoginRequest request){
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

        return new LoginResult(accessToken, refreshToken);
    }

    // 리프레쉬 토큰으로 액세스 토큰 재발급 (로그인 유지용)
    public LoginResponse refresh(String refreshToken){

        // 디비에 리프레쉬 토큰 있는지 확인
        RefreshToken savedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()->new BusinessException(
                        ErrorCode.INVALID_REFRESH_TOKEN
                ));

        // 들어온 토큰 검증
        if (!jwtProvider.validateToken(refreshToken)){
            throw new BusinessException(
                    ErrorCode.INVALID_REFRESH_TOKEN
            );
        }

        // 디비에 해당하는 리프레쉬 토큰 존재하고, 검증되면
        String accessToken = jwtProvider.createAccessToken(
                savedToken.getUserId()
        );

        // 액세스 토큰을 응답으로 돌려줌
        return new LoginResponse(accessToken);
    }

    // 로그아웃 처리
    // HttpOnly Cookie에 저장된 refreshToken을 무효화
    // 쿠키를 제거
    @Transactional
    public void logout(Long userId){
        // 토큰이 없어도 에러 나지 않고 로그아웃 성공으로
        // 토큰이 있으면 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }
}
