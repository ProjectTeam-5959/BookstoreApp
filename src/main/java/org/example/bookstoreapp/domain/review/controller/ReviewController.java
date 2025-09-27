package org.example.bookstoreapp.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.review.dto.request.ReviewRequest;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.service.ReviewService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성 - 인증인가 로직이 구현되었을 경우 userId 제거
    @PostMapping("/books/{bookId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewRequest ReviewRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(authUser, bookId, ReviewRequest));
    }

    // 로그인 유저를 기준으로 리뷰 전체 조회
    @GetMapping("/reviews")
    public ResponseEntity<Slice<ReviewResponse>> getReviews(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(sort = "modifiedAt",direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(reviewService.getReviews(authUser, pageable));
    }

    // 리뷰 수정
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest ReviewRequest
    ) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, ReviewRequest));
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
