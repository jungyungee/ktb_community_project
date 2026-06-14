package com.ktb.community.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateRequest {
    @NotBlank
    private String newPassword;
}
