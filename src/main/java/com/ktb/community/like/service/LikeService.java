package com.ktb.community.like.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.like.dto.LikeResponse;
import com.ktb.community.like.entity.Like;
import com.ktb.community.like.repository.LikeRepository;
import com.ktb.community.post.entity.Post;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.entity.User;
import com.ktb.community.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 좋아요 추가 및 취소
    // post, delete 로 구분하지 않고, 이미 좋아요된 게시글이라면 자동으로 취소
    @Transactional // 실제로 좋아요 추가 및 삭제와 카운트 증가, 감소를 하나의 작업으로 해야함 -> 트랜잭션
    public LikeResponse toggleLike(Long userId, Long postId){
        // 유저 정보 검증 (로그인 검증)
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // 좋아요 주체 사용자 확인을 위한 사용자 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 좋아요할 게시글 확인을 위한 게시글 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 이미 좋아요 했는지 확인
        Optional<Like> likeExist = likeRepository.findByUserIdAndPostId(userId, postId);

        // 좋아요 취소
        if (likeExist.isPresent()){
            // 이미 좋아요 한 엔티티면 삭제
            likeRepository.delete(likeExist.get());
            // likeCount도 하나 내림
            post.decreaseLikeCount();

            return new LikeResponse(
                    post.getId(),
                    post.getLikeCount(),
                    false
            );
        }

        // 좋아요 추가
        Like like = new Like(
                user,
                post
        );

        likeRepository.save(like);
        post.increaseLikeCount();

        return new LikeResponse(
                post.getId(),
                post.getLikeCount(),
                true
        );
    }
}
