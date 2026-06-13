package com.ktb.community.comment.service;

import com.ktb.community.comment.dto.CommentRequest;
import com.ktb.community.comment.dto.CommentResponse;
import com.ktb.community.comment.entity.Comment;
import com.ktb.community.comment.repository.CommentRepository;
import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.post.entity.Post;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.entity.User;
import com.ktb.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 추가
    public CommentResponse createComment(Long userId, Long postId, CommentRequest request){
        // 유저 로그인 상태 확인
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        // userId를 통해 user 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Post Id를 통해 가져온 post
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 해당하는 Post에 댓글 작성
        Comment comment = new Comment(
            user, post, request.getContent()
        );

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }
}
