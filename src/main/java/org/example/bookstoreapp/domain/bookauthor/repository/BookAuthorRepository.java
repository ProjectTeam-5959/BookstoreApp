package org.example.bookstoreapp.domain.bookauthor.repository;

import org.example.bookstoreapp.domain.book.entity.ContributorRole;
import org.example.bookstoreapp.domain.bookauthor.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    List<BookAuthor> findAllByBook_Id(
            Long bookId
    );

    List<BookAuthor> findAllByAuthor_Id(
            Long authorId
    );

    boolean existsByBook_IdAndAuthor_IdAndRole(
            Long bookId, Long authorId, ContributorRole role
    );
}
