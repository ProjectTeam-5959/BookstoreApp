package org.example.bookstoreapp.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.example.bookstoreapp.domain.review.entity.QReview.review;
import static org.example.bookstoreapp.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // offset 방식으로 구현
    @Override
    public Slice<Review> findByUserId(Long userId, Pageable pageable) {
        List<Review> reviews = jpaQueryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin()
                .where(review.user.id.eq(userId), review.deleted.eq(false))
                .orderBy(review.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();

        if (hasNext) {
            reviews.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(reviews, pageable, hasNext);
    }

    @Override
    public Slice<Review> findByBookId(Long bookId, Pageable pageable) {
        return null;
    }
}