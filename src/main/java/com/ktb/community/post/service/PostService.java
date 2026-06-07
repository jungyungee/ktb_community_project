package com.ktb.community.post.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.user.entity.User;
import com.ktb.community.post.dto.PostRequest;
import com.ktb.community.post.dto.PostResponse;
import com.ktb.community.post.entity.Post;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 게시물 저장 관련 비즈니스 로직 수행
@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    // 유저 Id에 따라 로그인 한 유저를 확인
    // (필터에서 userId가 request에 저장되어 있음)
    // Post 엔티티 생성
    // PostRepository 불러서 저장
    // 응답 반환
    public PostResponse createPost(Long userId, PostRequest request){
        // User 객체에 userId를 통해 user 찾아서 저장
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Post 엔티티 생성
        Post post = new Post(
                user,
                request.getTitle(),
                request.getContent(),
                request.getPostImageUrl()
        );

        // Post 내용 저장
        Post savedPost = postRepository.save(post);

        // 응답 반환
        return new PostResponse(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                savedPost.getPostImageUrl(),
                savedPost.getCreatedAt()
        );
    }
}
