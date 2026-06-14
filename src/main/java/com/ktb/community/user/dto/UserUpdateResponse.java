package com.ktb.community.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {
    private Long id;
    private String nickname;
    private String profileImageUrl;
}
