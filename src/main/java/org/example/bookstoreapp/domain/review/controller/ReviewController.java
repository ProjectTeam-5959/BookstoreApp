package org.example.bookstoreapp.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.response.ApiResponse;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.service.ReviewService;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {
        ReviewResponse response = reviewService.createReview(authUser, bookId, reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("리뷰 작성 완료", response));
    }

    // 로그인 유저를 기준으로 리뷰 전체 조회
    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Slice<ReviewResponse>>> getReviews(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(required = false) Long lastReviewId,
            @RequestParam(required = false) LocalDateTime lastModifiedAt,
            @RequestParam(defaultValue = "10") int size
    ) {
        Slice<ReviewResponse> reviews = reviewService.getReviews(authUser, lastReviewId, lastModifiedAt, size);
        return ResponseEntity.ok(ApiResponse.success("리뷰 조회 완료", reviews));
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ) {
        ReviewResponse response = reviewService.updateReview(authUser, reviewId, reviewRequest);
        return ResponseEntity.ok(ApiResponse.success("리뷰 수정 완료", response));
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(authUser, reviewId);
        return ResponseEntity.ok(ApiResponse.success("리뷰 삭제 완료"));
    }
}
