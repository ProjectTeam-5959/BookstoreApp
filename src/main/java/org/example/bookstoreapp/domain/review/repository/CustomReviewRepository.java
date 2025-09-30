package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomReviewRepository {
    // 슬라이스를 활용하여 무한 스크롤을 구현
    Slice<Review> findByUserId(Long userId, Pageable pageable);

    Slice<Review> findByBookId(Long bookId, Pageable pageable);

    /**
     * Todo : 커스텀 퀴리 메서드 활용하면서 N+1 문제 같이 해결하기 -> 대규모 작업에선 이 방법이 더 적합
     * Todo : 서비스 계층에서도 적용해보는 것도 방법 중 하나이다. 비교해서 생각해보기! -> 소규모일 땐 이 방법이 더 적합
     */
}
