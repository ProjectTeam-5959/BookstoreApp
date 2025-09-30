package org.example.bookstoreapp.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.example.bookstoreapp.domain.review.exception.ReviewErrorCode;
import org.example.bookstoreapp.domain.review.repository.ReviewRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // 리뷰 작성
    public ReviewResponse createReview(
            AuthUser authUser,
            Long bookId,
            ReviewRequest reviewRequest
    ) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_USER)
        );

        Book book = bookRepository.findByIdAndDeletedFalse(bookId).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_BOOK)
        );

        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .user(user)
                .book(book)
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    // 리뷰 조회
    @Transactional(readOnly = true)
    public Slice<ReviewResponse> getReviews(
            AuthUser authUser,
            Long lastReviewId,
            LocalDateTime lastModifiedAt,
            int size
    ) {
        Slice<Review> reviews = reviewRepository.findByUserId(authUser.getId(), lastReviewId, lastModifiedAt, size);
        return reviews.map(ReviewResponse::from);
    }

    // 리뷰 수정
    public ReviewResponse updateReview(
            AuthUser authUser,
            Long reviewId,
            ReviewRequest reviewRequest
    ) {
        Review review = reviewRepository.findByIdAndDeletedFalse(reviewId).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_REVIEW)
        );

        if (!Objects.equals(review.getUser().getId(), authUser.getId())) {
            throw new BusinessException(ReviewErrorCode.FORBIDDEN_ACCESS_REVIEW); // 예시 에러 코드
        }

        review.updateReview(reviewRequest);
        return ReviewResponse.from(review);
    }

    // 리뷰 삭제 (soft delete 적용)
    public void deleteReview(
            AuthUser authUser,
            Long reviewId
    ) {
        Review review = reviewRepository.findByIdAndDeletedFalse(reviewId).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_REVIEW)
        );

        if (!Objects.equals(review.getUser().getId(), authUser.getId())) {
            throw new BusinessException(ReviewErrorCode.FORBIDDEN_ACCESS_REVIEW); // 예시 에러 코드
        }

        review.softDelete();
    }
}
