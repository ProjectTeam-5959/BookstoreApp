package org.example.bookstoreapp.domain.library.repository;

import org.example.bookstoreapp.domain.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
