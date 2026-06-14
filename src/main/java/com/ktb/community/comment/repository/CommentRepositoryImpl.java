package com.ktb.community.comment.repository;

import com.ktb.community.comment.entity.Comment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.ktb.community.comment.entity.QComment.comment;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    // cursor (이전 마지막 댓글) 이 없으면 최신 순으로 처음부터 조회
    // cursor 있으면 그 다음부터 createdAt, id 두 조건을 이용해 조회
    // createdAt DESC, id DESC 정렬
    // 다음을 위해 (hasNext) 를 주기 위해 size+1 개 조회

    @Override
    public List<Comment> findCommentsByCursor(Long postId, LocalDateTime createdAt, Long id, int size){
        // Where 절을 위한 객체 생성
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(comment.post.id.eq(postId));

        // 첫 댓글이면 커서가 없음
        // post의 createdAt 보다 작은 것, 혹은 같다면 id가 작은 것부터
        if (createdAt != null && id != null){
            condition.and(
                    comment.createdAt.lt(createdAt)
                            .or(
                                    comment.createdAt.eq(createdAt)
                                            .and(comment.id.lt(id))
                            )
            );
        }

        // comment에서 위에 정의한 condition에 맞게 가져오고, 정렬해서 내보내기
        return queryFactory
                .selectFrom(comment)
                .join(comment.user).fetchJoin()
                .where(condition)
                .orderBy(
                        comment.createdAt.desc(),
                        comment.id.desc()
                )
                .limit(size + 1)
                .fetch();
    }

}
