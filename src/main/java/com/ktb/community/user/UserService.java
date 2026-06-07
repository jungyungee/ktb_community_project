package com.ktb.community.user;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.global.security.PasswordHash;
import com.ktb.community.user.dto.SignupRequest;
import com.ktb.community.user.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 실제 유저 관련 비즈니스 로직을 수행(회원가입 로직)
@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHash passwordHash;

    // SignupRequest를 받고 이메일과 닉네임 중복을 확인 (Repository에 보냄)
    // 중복 검사를 통과하면, User 엔티티를 만들고
    // UserRepository를 통해 저장
    // 이후, 응답객체 dto를 통해 반환
    public SignupResponse signup(SignupRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            // 중복 이메일 처리
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(request.getNickname())){
            // 중복 닉네임 처리
            throw new BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 중복 확인 완료 시
        // 비밀번호 해싱
        String hashedPassword = passwordHash.hash(request.getPassword());

        // 들어온 값으로 user 객체 생성
        User user = new User(request.getEmail(), hashedPassword, request.getNickname(), request.getProfileImageUrl());
        // DB에 저장 (userRepository.save(user)의 결과를 savedUser에 담아서 응답 DTO 만듦)
        User savedUser = userRepository.save(user);
        // 응답 반환
        return new SignupResponse(savedUser.getId());
    }
}
