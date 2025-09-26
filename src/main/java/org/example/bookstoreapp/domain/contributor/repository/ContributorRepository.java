package org.example.bookstoreapp.domain.contributor.repository;

import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContributorRepository extends JpaRepository<Contributor,Long> {
    Optional<Contributor> findByName(String name);
    boolean existsByName(String name);
}
