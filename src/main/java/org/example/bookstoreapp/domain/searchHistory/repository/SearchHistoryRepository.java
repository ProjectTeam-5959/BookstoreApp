package org.example.bookstoreapp.domain.searchHistory.repository;

import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Page<SearchHistory> findByUserId(Long userId, Pageable pageable);

    // 인기 키워드 title별 조회
    @Query("""
            SELECT sh.title AS keyword, COUNT(sh.title) AS cnt
            FROM SearchHistory sh
            WHERE sh.title IS NOT NULL
            GROUP BY sh.title
            ORDER BY COUNT(sh.title) DESC
            """)
    Page<PopularKeywordCount> findPopularTitles(Pageable pageable);

    // 인기 키워드 name별 조회
    @Query("""
            SELECT sh.name AS keyword, COUNT(sh.name) AS cnt
            FROM SearchHistory sh
            WHERE sh.name IS NOT NULL
            GROUP BY sh.name
            ORDER BY COUNT(sh.name) DESC
            """)
    Page<PopularKeywordCount> findPopularNames(Pageable pageable);

    // 인기 키워드 category별 조회
    @Query("""
        SELECT sh.category AS keyword, COUNT(sh.category) AS cnt
        FROM SearchHistory sh
        WHERE sh.category IS NOT NULL
        GROUP BY sh.category
        ORDER BY COUNT(sh.category) DESC
        """)
    Page<PopularKeywordCount> findPopularCategories(Pageable pageable);

    List<SearchHistory> findAllByUserId(Long userId);

    interface PopularKeywordCount {
        String getKeyword();
        Long getCnt();
    }
}