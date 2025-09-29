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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_BOOK)
        );

        if (book.isDeleted()) {
            throw new BusinessException(ReviewErrorCode.NOT_FOUND_BOOK);
        }

        Review review = Review.builder()
                .content(reviewRequest.getContent())
                .user(user)
                .book(book)
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewResponse.from(savedReview);
    }

    // 로그인 유저를 기준으로 리뷰 전체 조회 - 현재 offset 기반의 페이지네이션 적용 추후 Cursor 기반의 페이지네이션 적용 고려!
    /**
     * Todo : 책이 삭제되었거나 삭제된 유저라면 softDelete - true로 변환되고 출력 X
     */
    @Transactional(readOnly = true)
    public Slice<ReviewResponse> getReviews(
            AuthUser authUser,
            Pageable pageable
    ) {
        Slice<Review> reviews = reviewRepository.findByUserId(authUser.getId(), pageable);
        return reviews.map(ReviewResponse::from);
    }

    // 리뷰 수정
    public ReviewResponse updateReview(
            AuthUser authUser,
            Long reviewId,
            ReviewRequest reviewRequest
    ) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
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
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new BusinessException(ReviewErrorCode.NOT_FOUND_REVIEW)
        );

        if (!Objects.equals(review.getUser().getId(), authUser.getId())) {
            throw new BusinessException(ReviewErrorCode.FORBIDDEN_ACCESS_REVIEW); // 예시 에러 코드
        }

        review.softDelete();
    }
}
