package org.example.bookstoreapp.domain.contributor.repository;

import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {
}
