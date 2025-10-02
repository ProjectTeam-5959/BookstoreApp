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

    @Override
    public Slice<Review> findByUserId(Long userId, Long lastReviewId, LocalDateTime lastModifiedAt, int size) {
        List<Review> reviews = jpaQueryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin()
                .where(
                        review.user.id.eq(userId),
                        review.deleted.eq(false),
                        (lastReviewId != null && lastModifiedAt != null) ?
                                new BooleanBuilder()
                                        .and(review.modifiedAt.lt(lastModifiedAt))
                                        .or(review.modifiedAt.eq(lastModifiedAt)).and(review.id.lt(lastReviewId))
                                : null
                )
                .orderBy(review.modifiedAt.desc(), review.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = reviews.size() > size;

        if (hasNext) {
            reviews.remove(size);
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
                        (lastReviewId != null && lastModifiedAt != null) ?
                                new BooleanBuilder()
                                        .and(review.modifiedAt.lt(lastModifiedAt))
                                        .or(review.modifiedAt.eq(lastModifiedAt)).and(review.id.lt(lastReviewId))
                                : null
                )
                .orderBy(review.modifiedAt.desc(), review.id.desc())
                .limit(size + 1)
                .fetch();

        boolean hasNext = reviews.size() > size;

        if (hasNext) {
            reviews.remove(size);
        }

        return new SliceImpl<>(reviews, PageRequest.of(0, size), hasNext);
    }
}