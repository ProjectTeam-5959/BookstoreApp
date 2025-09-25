package org.example.bookstoreapp.domain.author.repository;

import org.example.bookstoreapp.domain.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByName(
            String name
    );

    boolean existsByName(
            String name
    );
}
