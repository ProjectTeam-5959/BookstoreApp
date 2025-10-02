package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Review> findByIdAndDeletedFalse(Long id);

    @Modifying
    @Query("""
            UPDATE
            Review r
            SET r.deleted = true,
            r.modifiedAt = CURRENT_TIMESTAMP
            WHERE r.book.id = :bookId
            AND r.deleted = false
            """)
    int softDeleteByBookId(@Param("bookId") Long bookId);
}
