package org.example.bookstoreapp.domain.library.repository;

import org.example.bookstoreapp.domain.library.entity.LibraryBook;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {

    // 삭제된 책까지 조회 가능
    // 엔티티에 @SQLRestriction 어노테이션 때문에 삭제된 책이 조회 안됨
    // Native Query 사용 시 가능! (JPQL 사용 시 500 에러 발생)
    @Query(
            value = "SELECT * FROM librarybooks " +
                    "WHERE library_id = :libraryId " +
                    "AND book_id = :bookId",
            nativeQuery = true
    )
    LibraryBook findEvenDeleted(Long libraryId, Long bookId);

    // 무한스크롤 적용 (서재 조회)
    Slice<LibraryBook> findByLibraryId(Long libraryId, Pageable pageable);
}
