package com.ktb.community.comment.service;

import com.ktb.community.comment.cursor.CommentCursor;
import com.ktb.community.comment.dto.*;
import com.ktb.community.comment.entity.Comment;
import com.ktb.community.comment.repository.CommentRepository;
import com.ktb.community.global.exception.BusinessException;
import com.ktb.community.global.exception.ErrorCode;
import com.ktb.community.post.entity.Post;
import com.ktb.community.user.entity.User;
import com.ktb.community.post.repository.PostRepository;
import com.ktb.community.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 댓글 추가
    @Transactional
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
        // 게시글 내 댓글 카운트 추가
        post.increaseCommentCount();

        return new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }

    // 댓글 리스트 조회
    public CommentListResponse getCommentList(String cursor, Long postId, Long userId){
        // 댓글은 유저 (isOwner 필요), 게시글 (댓글과 연결) 필요
        // 로그인 여부 확인
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        // 유저 정보 확인
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 게시글 정보 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new BusinessException(ErrorCode.POST_NOT_FOUND));

        // 페이징 구현
        int size = 10; //길이 10
        // 첫 페이지 경우를 위해 null로 넣음
        LocalDateTime cursorCreatedAt = null;
        Long cursorId = null;

        if (cursor != null && !cursor.isBlank()){
            CommentCursor commentCursor = decodeCursor(cursor);
            cursorCreatedAt = LocalDateTime.parse(commentCursor.getCreatedAt());
            cursorId = commentCursor.getId();
        }

        // 댓글 조회
        // 첫 페이지면 둘다 null로,
        // cursor가 있으면 여기서 Base64 디코딩해서 cursorCreatedAt, cursorId를 꺼내서
        // findCommentsByCursor 로 찾음
        List<Comment> comments = commentRepository.findCommentsByCursor(
                postId,
                cursorCreatedAt,
                cursorId,
                size
        );

        // 마지막 리스트인지 확인
        // 조회 결과가 size 보다 많으면 다음 페이지가 존재 (size +1 만큼 받아오므로)
        boolean hasNext = comments.size() > size;
        if (hasNext){
            comments = comments.subList(0, size);
        }

        // 다음 페이지 조회에 사용할 cursor 값
        String nextCursor = null;
        if (hasNext) {
            // 현재 응답 목록의 마지막 댓글을 기준으로 nextCursor를 생성
            Comment lastComment = comments.get(comments.size() - 1);
            // cursor는 정렬 기준인 createdAt과 id 를 이용
            CommentCursor commentCursor = new CommentCursor(
                    lastComment.getCreatedAt().toString(),
                    lastComment.getId()
            );
            // CommentCursor를 문자열로 변환한 뒤 Base64로 인코딩
            nextCursor = encodeCursor(commentCursor);
        }

        // List 내 각 Item에 들어갈 값들
        List<CommentItemResponse> content = comments.stream()
                .map(comment -> new CommentItemResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        new CommentAuthorResponse(
                                comment.getUser().getNickname(),
                                comment.getUser().getProfileImageUrl()
                        ),
                        comment.getUser().getId().equals(userId)
                ))
                .toList();

        return new CommentListResponse(
                content,
                nextCursor,
                hasNext
        );
    }

    // 커서 인코딩
    private String encodeCursor(CommentCursor cursor) {
        String value = cursor.getCreatedAt() + "|" + cursor.getId();
        return Base64.getUrlEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    // 커서 디코딩
    private CommentCursor decodeCursor(String cursor){
        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = decoded.split("\\|");

            if (parts.length != 2) {
                throw new BusinessException(ErrorCode.INVALID_CURSOR);
            }

            return new CommentCursor(
                    parts[0],
                    Long.parseLong(parts[1])
            );

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_CURSOR);
        }
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long userId, Long commentId, CommentRequest request){
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        // comment Id를 통해 가져온 comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 댓글의 작성자와 현재 로그인된 사용자가 일치하지 않을 시, 수정 불가
        if (!comment.getUser().getId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_COMMENT_OWNER);
        }

        comment.update(
                request.getContent()
        );

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId){
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
        if(!comment.getUser().getId().equals(userId)){
            throw new BusinessException(ErrorCode.NOT_COMMENT_OWNER);
        }
        commentRepository.delete(comment);
    }
}
