package com.ktb.community.post.service;

import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.like.repository.LikeRepository;
import com.ktb.community.post.cursor.PostCursor;
import com.ktb.community.post.dto.*;
import com.ktb.community.user.entity.User;
import com.ktb.community.post.entity.Post;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

// 게시물 저장 관련 비즈니스 로직 수행
@Service
@RequiredArgsConstructor // 생성자 자동 생성
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

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

        if (cursor != null && !cursor.isBlank()){
            PostCursor postCursor = decodeCursor(cursor);
            cursorCreatedAt = LocalDateTime.parse(postCursor.getCreatedAt());
            cursorId = postCursor.getId();
        }

        // 게시글 조회
        // 첫 페이지면 둘다 null로,
        // cursor가 있으면 여기서 Base64 디코딩해서 cursorCreatedAt, cursorId를 꺼내서
        // findPostsByCursor 로 찾음
        List<Post> posts = postRepository.findPostsByCursor(
                cursorCreatedAt,
                cursorId,
                size
        );

        // 마지막 리스트인지 확인
        // 조회 결과가 size 보다 많으면 다음 페이지가 존재 (size +1 만큼 받아오므로)
        boolean hasNext = posts.size() > size;
        if (hasNext){
            posts = posts.subList(0, size);
        }

        // 다음 페이지 조회에 사용할 cursor 값
        String nextCursor = null;
        if (hasNext) {
            // 현재 응답 목록의 마지막 게시글을 기준으로 nextCursor를 생성
            Post lastPost = posts.get(posts.size() - 1);
            // cursor는 정렬 기준인 createdAt과 id 를 이용
            PostCursor postCursor = new PostCursor(
                    lastPost.getCreatedAt().toString(),
                    lastPost.getId()
            );
            // PostCursor를 문자열로 변환한 뒤 Base64로 인코딩
            nextCursor = encodeCursor(postCursor);
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
                nextCursor,
                hasNext
        );
    }

    // 커서 인코딩
    private String encodeCursor(PostCursor cursor) {
        String value = cursor.getCreatedAt() + "|" + cursor.getId();

        return Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    // 커서 디코딩
    private PostCursor decodeCursor(String cursor){
        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = decoded.split("\\|");

            if (parts.length != 2) {
                throw new BusinessException(ErrorCode.INVALID_CURSOR);
            }

            return new PostCursor(
                    parts[0],
                    Long.parseLong(parts[1])
            );

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_CURSOR);
        }
    }

    // 게시물 단건 상세 조회
    @Transactional // 조회 수 구현을 위한 변경 감지를 위해 트랜잭션 추가
    public PostDetailResponse getPost(Long userId, Long postId){
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(()->
                        new BusinessException(ErrorCode.POST_NOT_FOUND)
                );
        int before = post.getViewCount();
        // 조회수 증가
        post.increaseViewCount();
        int after = post.getViewCount();

        System.out.printf(
                "[%s] Post=%d EntityHash=%d ViewCount %d -> %d%n",
                Thread.currentThread().getName(),
                postId,
                System.identityHashCode(post),
                before,
                after
        );

        // 사용자 작성 게시글 여부 (수정, 삭제 권한을 위해)
        boolean isOwner = post.getUser().getId().equals(userId);
        // 사용자 좋아요 여부 (좋아요 중복 불가 및 취소 처리를 위해)
        boolean liked = likeRepository.existsByUserIdAndPostId(userId, postId);

        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPostImageUrl(),
                post.getCreatedAt(),
                new PostAuthorResponse(
                        post.getUser().getNickname(),
                        post.getUser().getProfileImageUrl()
                ),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getViewCount(),
                liked,
                isOwner
        );
    }

    // 게시글 수정
    // 트랜잭션을 활용 -> 영속성 컨텍스트의 변경감지를 활용해서 업데이트 가능
    @Transactional
    public PostUpdateResponse updatePost(Long userId, Long postId, PostUpdateRequest request){
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // Post Id를 통해 가져온 post에 새로운 값 업데이트
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        // 게시글의 작성자와 현재 로그인된 사용자가 일치하지 않을 시, 수정 불가
        if (!post.getUser().getId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_POST_OWNER);
        }

        // 업데이트 메서드
        post.update(
                request.getTitle(),
                request.getContent(),
                request.getPostImageUrl(),
                request.getDeleteImage()
        );

        return new PostUpdateResponse(
                post.getId()
        );
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long userId, Long postId){
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        // Post Id를 통해 가져온 post
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));
        // 게시글의 작성자와 현재 로그인된 사용자가 일치하지 않을 시, 삭제 불가
        if (!post.getUser().getId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_POST_OWNER);
        }
        // 게시글 삭제
        postRepository.delete(post);
    }
}
