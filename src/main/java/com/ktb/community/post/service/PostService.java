package com.ktb.community.post.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.post.dto.*;
import com.ktb.community.user.entity.User;
import com.ktb.community.post.entity.Post;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

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

    // 게시글 조회 로직
    public PostListResponse getPostList(String cursor) {
        int size = 10; // 길이 10
        // 첫 페이지 경우 생각해서 null로 넣음
        LocalDateTime cursorCreatedAt = null;
        Long cursorId = null;

        // cursor가 있으면 여기서 Base64 디코딩해서 cursorCreatedAt, cursorId를 꺼내서
        // findPostsByCursor 로 찾음
        List<Post> posts = postRepository.findPostsByCursor(
                cursorCreatedAt,
                cursorId,
                size
        );

        boolean hasNext = posts.size() > size;
        if (hasNext){
            posts = posts.subList(0, size);
        }

        // List 내 각 Item 에 들어갈 값들
        List<PostItemResponse> content = posts.stream()
                .map(post -> new PostItemResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getLikeCount(),
                        post.getCommentCount(),
                        post.getViewCount(),
                        post.getCreatedAt(),
                        new PostAuthorResponse(
                                post.getUser().getNickname(),
                                post.getUser().getProfileImageUrl()
                        )
                ))
                .toList();

        return new PostListResponse(
                content,
                null,
                hasNext
        );
    }
}
