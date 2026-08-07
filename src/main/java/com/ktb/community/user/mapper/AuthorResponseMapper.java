package com.ktb.community.user.mapper;

import com.ktb.community.post.dto.PostAuthorResponse;
import com.ktb.community.user.entity.User;
import com.ktb.community.user.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthorResponseMapper {
    // soft delete 한 유저 정보를 마스킹 (닉네임 알 수 없음, 이미지 null 처리)
    private static final String DELETED_USER_NICKNAME = "알 수 없음";
    private static final String DELETED_USER_PROFILE_IMAGE_URL = null;

    public PostAuthorResponse toPostAuthorResponse(User user) {
        if (user.getStatus() == UserStatus.DELETED) {
            return new PostAuthorResponse(DELETED_USER_NICKNAME, DELETED_USER_PROFILE_IMAGE_URL);
        }
        return new PostAuthorResponse(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
