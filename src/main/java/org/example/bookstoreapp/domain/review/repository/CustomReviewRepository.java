package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface CustomReviewRepository {
    Slice<Review> findByUserId(Long userId, Long lastReviewId, LocalDateTime lastModifiedAt, int size);
    Slice<Review> findByBookId(Long bookId, Long lastReviewId, LocalDateTime lastModifiedAt, int size);
}
