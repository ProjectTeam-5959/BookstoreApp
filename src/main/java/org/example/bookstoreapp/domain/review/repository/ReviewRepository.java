package org.example.bookstoreapp.domain.review.repository;

import org.example.bookstoreapp.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    Optional<Review> findByIdAndDeletedFalse(Long id);

    @Modifying // 도서와 연관된 리뷰의 정보를 한 번에 조회와 함께 리뷰의 상태를 변환하는 쿼리
    @Query("""
            UPDATE
            Review r
            SET r.deleted = true,
            r.modifiedAt = CURRENT_TIMESTAMP
            WHERE r.book.id = :bookId
            AND r.deleted = false
            """)
    int softDeleteByBookId(@Param("bookId") Long bookId); // int - 데이터베이스에서 변경한 행의 개수를 반환하기 때문에.. void - 반환 값이 필요없을 때 사용
}
