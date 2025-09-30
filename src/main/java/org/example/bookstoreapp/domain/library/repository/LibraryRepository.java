package org.example.bookstoreapp.domain.library.repository;

import org.example.bookstoreapp.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface
LibraryRepository extends JpaRepository<Library, Long> {

    @Query("SELECT DISTINCT l FROM Library l " +
            "LEFT JOIN FETCH l.libraryBooks lb " +
            "WHERE l.user.id = :userId")
    Optional<Library> findByUserId(Long userId);
}

/*
            @Query("SELECT DISTINCT l FROM Library l " +
            "LEFT JOIN FETCH l.libraryBooks lb " +
            "LEFT JOIN FETCH lb.book b " +
            "LEFT JOIN FETCH b.bookContributors bc " +
            "LEFT JOIN FETCH bc.contributor c " +
            "WHERE l.user.id = :userId")

            -> 컬렉션(Library -> LibraryBooks)과 컬렉션(Book -> BookContributors)을
               동시에 fetch join 하면 에러 발생!
            => 컬렉션 fetch join 은 한 쿼리에서 하나만 가능!

            => 추후 페이지 적용을 위해
               쿼리 2개 써서 N+1 해결 보다는 BatchSize 사용 예정
 */