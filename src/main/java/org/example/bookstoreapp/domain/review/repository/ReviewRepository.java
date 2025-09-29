package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 슬라이스를 활용하여 무한 스크롤을 구현
    Slice<Review> findByUserId(Long userId, Pageable pageable);

    Slice<Review> findByBookId(Long bookId, Pageable pageable);
}
