package com.ktb.community.post.repository;

import com.ktb.community.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.ktb.community.post.entity.QPost.post;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
// PostRepositoryCustom의 구현체 (Spring Data JPA Convention)
public class PostRepositoryImpl implements PostRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    // cursor (이전 마지막 게시글) 이 없으면 최신순으로 처음부터 조회
    // cursor 있으면 그 다음부터 createdAt, id 두 조건을 이용해 조회
    // createdAt DESC, id DESC 정렬
    // 다음을 위해 (hasNext) 를 주기 위해 size+1 개 조회
    @Override
    public List<Post> findPostsByCursor(LocalDateTime createdAt, Long id, int size) {
        // Where 절을 위한 객체 생성
        BooleanBuilder condition = new BooleanBuilder();

        // 첫 게시글이면 커서가 없음. 조건이 없음
        // post의 createdAt 보다 작은 것 혹은 같다면 id가 작은 것부터
        if (createdAt != null && id != null){
            condition.and(
                    post.createdAt.lt(createdAt)
                            .or(
                                    post.createdAt.eq(createdAt)
                                            .and(post.id.lt(id))
                            )
            );
        }

        // post에서 위에 정의한 condition에 맞게 가져오고, 정렬해서 내보내기
        return queryFactory
                .selectFrom(post)
                .join(post.user).fetchJoin()
                .where(condition)
                .orderBy(
                        post.createdAt.desc(),
                        post.id.desc()
                )
                .limit(size + 1)
                .fetch();
    }

    // 조회수 증가 로직 - 원자적 DB 업데이트
    @Override
    public long increaseViewCount(Long id) {
        return queryFactory
                .update(post)
                .set(post.viewCount, post.viewCount.add(1))
                .where(post.id.eq(id))
                .execute();
    }

    // 좋아요 수 증가 로직 - 원자적 DB 업데이트
    @Override
    public long increaseLikeCount(Long id) {
        return queryFactory
                .update(post)
                .set(post.likeCount, post.likeCount.add(1))
                .where(post.id.eq(id))
                .execute();
    }

    // 좋아요 수 감소 로직 - 원자적 DB 업데이트
    @Override
    public long decreaseLikeCount(Long id) {
        return queryFactory
                .update(post)
                .set(post.likeCount, post.likeCount.subtract(1))
                .where(
                        post.id.eq(id),
                        post.likeCount.gt(0)
                )
                .execute();
    }

    // 댓글 수 증가 로직 - 원자적 DB 업데이트
    @Override
    public long increaseCommentCount(Long id) {
        return queryFactory
                .update(post)
                .set(post.commentCount, post.commentCount.add(1))
                .where(post.id.eq(id))
                .execute();
    }

    // 댓글 수 감소 로직 - 원자적 DB 업데이트
    @Override
    public long decreaseCommentCount(Long id) {
        return queryFactory
                .update(post)
                .set(post.commentCount, post.commentCount.subtract(1))
                .where(
                        post.id.eq(id),
                        post.commentCount.gt(0)
                )
                .execute();
    }
}
