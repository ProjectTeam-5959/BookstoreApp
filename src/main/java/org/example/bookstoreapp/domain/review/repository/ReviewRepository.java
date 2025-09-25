package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 페이지네이션 (슬라이스) 활용
}
