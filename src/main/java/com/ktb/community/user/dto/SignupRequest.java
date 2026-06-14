package com.ktb.community.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

// 회원가입 요청 JSON을 담는 객체
@Getter
public class SignupRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "password_format_invalid"
    )
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String profileImageUrl;
}
