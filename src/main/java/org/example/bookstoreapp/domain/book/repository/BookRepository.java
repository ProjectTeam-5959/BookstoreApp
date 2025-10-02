package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book>, CustomBookRepository {
    // ISBN으로 단일 도서 검색
    Optional<Book> findByIsbn(String isbn);

    // 특정 ISBN 존재 여부 확인
    boolean existsByIsbn(String isbn);

    // 키워드 검색
    @Query("""
            SELECT DISTINCT b
            FROM Book b
            JOIN FETCH b.bookContributors bc
            JOIN FETCH bc.contributor c
            WHERE (:title IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(:title), '%'))
            AND (:name IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%'))
            AND (:category IS NULL OR b.category = :category)
            """)
    Page<Book> findBooksByKeyword(
            @Param("title") String title,
            @Param("name") String name,
            @Param("category") BookCategory category,
            Pageable pageable
    );

    // 삭제되지 않은 도서 조회
    Optional<Book> findByIdAndDeletedFalse(Long id);
}