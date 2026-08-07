package com.ktb.community.user.mapper;

import com.ktb.community.comment.dto.CommentAuthorResponse;
import com.ktb.community.comment.entity.Comment;
import com.ktb.community.post.dto.PostAuthorResponse;
import com.ktb.community.user.entity.User;
import com.ktb.community.user.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthorResponseMapper {
    // soft delete 한 유저 정보를 마스킹 (닉네임 알 수 없음, 이미지 null 처리)
    private static final String DELETED_USER_NICKNAME = "알 수 없음";
    private static final String DELETED_USER_PROFILE_IMAGE_URL = null;

    // 게시글에서 작성자 정보 매핑
    public PostAuthorResponse toPostAuthorResponse(User user) {
        if (user.getStatus() == UserStatus.DELETED) {
            return new PostAuthorResponse(DELETED_USER_NICKNAME, DELETED_USER_PROFILE_IMAGE_URL);
        }
        return new PostAuthorResponse(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }

    // 댓글에서 작성자 정보 매핑
    public CommentAuthorResponse toCommentAuthorResponse(User user) {
        if (user.getStatus() == UserStatus.DELETED) {
            return new CommentAuthorResponse(
                    DELETED_USER_NICKNAME,
                    DELETED_USER_PROFILE_IMAGE_URL
            );
        }
        return new CommentAuthorResponse(
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
