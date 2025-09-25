package org.example.bookstoreapp.domain.user.repository;

import org.example.bookstoreapp.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
