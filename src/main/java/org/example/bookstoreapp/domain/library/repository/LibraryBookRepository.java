package org.example.bookstoreapp.domain.library.repository;

import org.example.bookstoreapp.domain.library.entity.LibraryBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {

    // 삭제된 책까지 조회 가능
    @Query(
            value = "SELECT * FROM librarybooks " +
                    "WHERE library_id = :libraryId " +
                    "AND book_id = :bookId",
            nativeQuery = true
    )
    LibraryBook findEvenDeleted(Long libraryId, Long bookId);

    // 무한스크롤 적용 (서재 조회)
    // 책 조회 N+1 해결
    @Query(
            "SELECT DISTINCT lb FROM LibraryBook lb " +
            "JOIN FETCH lb.book b " +
            "WHERE lb.library.id = :libraryId")
    Slice<LibraryBook> findByLibraryId(Long libraryId, Pageable pageable);

    // 내 서재에서 책 삭제 (filter 대신)
    @Query("SELECT lb FROM LibraryBook lb " +
            "JOIN FETCH lb.book b " +
            "JOIN lb.library l " +
            "WHERE l.id = :libraryId " +
            "AND b.id = :bookId " +
            "AND lb.deleted = false")
    Optional<LibraryBook> findByLibraryIdAndBookId(Long libraryId, Long bookId);
}