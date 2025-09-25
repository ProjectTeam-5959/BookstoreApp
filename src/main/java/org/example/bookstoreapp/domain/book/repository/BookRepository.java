package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.domain.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Integer> {
    Optional<Book> findByIsbn(
            String isbn
    );

    boolean existsByIsbn
            (String isbn
            );

    List<Book> findAllByTitle(
            String titlePart
    );
}
