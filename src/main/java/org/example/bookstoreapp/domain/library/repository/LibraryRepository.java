package org.example.bookstoreapp.domain.library.repository;

import org.example.bookstoreapp.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface
LibraryRepository extends JpaRepository<Library, Long> {

    //@Query("SELECT DISTINCT l FROM Library l " +
    //        "LEFT JOIN FETCH l.libraryBooks lb " +
    //       "WHERE l.user.id = :userId")
    // 페이징 전 : library.getLibraryBooks() 접근 때문에 이 쿼리로 N+1 해결
    // 페이징 후 : libraryBookRepository.findByLibraryId(libraryId, pageable)
    //            이거 자체가 쿼리에서 직접 LibraryBook을 가져옴  => N+1 발생안함
    Optional<Library> findByUserId(Long userId);
}