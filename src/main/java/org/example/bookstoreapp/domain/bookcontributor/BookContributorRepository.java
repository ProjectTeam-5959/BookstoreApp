package org.example.bookstoreapp.domain.bookcontributor;

import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookContributorRepository extends JpaRepository<BookContributor, Long> {
}
