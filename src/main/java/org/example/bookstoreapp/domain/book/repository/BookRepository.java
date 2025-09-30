package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {
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
            Pageable pageable);

    // 나의 검색어 기반 도서 Top10
    @Query("""
            SELECT DISTINCT b
            FROM Book b
            JOIN FETCH b.bookContributors bc
            JOIN FETCH bc.contributor c
            WHERE (:title IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(:title), '%'))
            AND (:name IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%'))
            AND (:category IS NULL OR b.category = :category)
            """)
    List<Book> findTop10BooksByMySearchHistory(
            @Param("title") String title,
            @Param("name") String name,
            @Param("category") BookCategory category,
            Pageable pageable
    );
}

/**
 * 1. JpaSpecificationExecutor<Book> 추가
 *    → 제목, 카테고리, 출판사 등 다양한 검색 조건을 Specification 기반으로 조합할 수 있음.
 *    → 메소드 네이밍(findByX) 방식으로는 관리가 어려운 복잡한 검색 조건을 해결.
 *
 * 2. findAllByTitle 제거 (List<Book> findAllByTitle(String title);)
 *    → 단일 조건(제목 일치)만 지원하므로 유연성이 부족.
 *    → 대신 BookSpecs.titleContains(...) 를 사용해 부분 검색(contains, 대소문자 무시) 가능.
 *
 * 3. 확장성 고려
 *    → 캐싱, 필터링, 복합 조건 검색을 고려했을 때 A안 구조가 유지보수성과 확장성에 더 적합.
 *
 * */