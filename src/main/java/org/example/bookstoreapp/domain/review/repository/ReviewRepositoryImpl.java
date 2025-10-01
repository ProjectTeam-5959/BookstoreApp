package org.example.bookstoreapp.domain.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.example.bookstoreapp.domain.book.entity.QBook.book;
import static org.example.bookstoreapp.domain.review.entity.QReview.review;
import static org.example.bookstoreapp.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // Cursor 방식으로 구현
    @Override
    public Slice<Review> findByUserId(Long userId, Long lastReviewId, LocalDateTime lastModifiedAt, int size) {
        List<Review> reviews = jpaQueryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin() // review와 user를 Fetch Join을 하여 N+1 방지
                .where(
                        review.user.id.eq(userId), // 1. 로그인한 사용자가 작성한 리뷰로 제한
                        review.deleted.eq(false), // 2. Soft Delete된 리뷰는 제외
                        (lastReviewId != null && lastModifiedAt != null) ? // 커서 조건
                                new BooleanBuilder()
                                        .and(review.modifiedAt.lt(lastModifiedAt)) // 1순위: 이전 커서의 modifiedAt 보다 더 오래된 데이터 -> lt() 는 '<' 이것과 같다. 즉, review.modifiedAt < lastModifiedAt;
                                        .or(review.modifiedAt.eq(lastModifiedAt)).and(review.id.lt(lastReviewId)) // 2순위: modifiedAt이 같으면 id가 작은 데이터
                                : null
                )
                .orderBy(review.modifiedAt.desc(), review.id.desc()) // 수정시각 기준으로 정렬 만약 수정시각이 같다면 아이디 순으로 내림차순
                .limit(size + 1) // 요청된 size 보다 하나 더 가져와서 다음 페이지가 있는 지 없는 지 판단할 때 사용
                .fetch();

        boolean hasNext = reviews.size() > size; // (size + 1)번째 데이터가 있다면 true

        if (hasNext) {
            reviews.remove(size); // (size + 1)번째 데이터는 확인용 그러므로 제거
        }

        return new SliceImpl<>(reviews, PageRequest.of(0, size), hasNext);
    }

    @Override
    public Slice<Review> findByBookId(Long bookId, Long lastReviewId, LocalDateTime lastModifiedAt, int size) {
        List<Review> reviews = jpaQueryFactory
                .selectFrom(review)
                .join(review.book, book).fetchJoin()
                .where(
                        review.book.id.eq(bookId),
                        review.deleted.eq(false),
                        (lastReviewId != null && lastModifiedAt != null) ? // 커서 조건
                                new BooleanBuilder()
                                        .and(review.modifiedAt.lt(lastModifiedAt)) // 1순위: 이전 커서의 modifiedAt 보다 더 오래된 데이터 -> lt() 는 '<' 이것과 같다. 즉, review.modifiedAt < lastModifiedAt;
                                        .or(review.modifiedAt.eq(lastModifiedAt)).and(review.id.lt(lastReviewId)) // 2순위: modifiedAt이 같으면 id가 작은 데이터
                                : null
                )
                .orderBy(review.modifiedAt.desc(), review.id.desc()) // 수정시각 기준으로 정렬 만약 수정시각이 같다면 아이디 순으로 내림차순
                .limit(size + 1)
                .fetch();

        boolean hasNext = reviews.size() > size;

        if (hasNext) {
            reviews.remove(size);
        }

        return new SliceImpl<>(reviews, PageRequest.of(0, size), hasNext);
    }
}