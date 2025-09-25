package org.example.bookstoreapp.domain.searchHistory.repository;

import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}