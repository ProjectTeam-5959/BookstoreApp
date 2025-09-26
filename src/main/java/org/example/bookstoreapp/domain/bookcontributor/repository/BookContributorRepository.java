package org.example.bookstoreapp.domain.bookcontributor.repository;

import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookContributorRepository extends JpaRepository<BookContributor, Long> {
    List<BookContributor> findAllByBook_Id(
            Long bookId
    );

    List<BookContributor> findAllByContributor_Id(
            Long authorId
    );

    boolean existsByBook_IdAndContributor_IdAndRole(
            Long bookId, Long authorId, ContributorRole role
    );
}
