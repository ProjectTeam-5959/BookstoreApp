package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Review> findByIdAndDeletedFalse(Long id);
}
