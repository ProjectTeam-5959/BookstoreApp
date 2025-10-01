package org.example.bookstoreapp.domain.bookcontributor;

import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookContributorRepository extends JpaRepository<BookContributor, Long> {

    // 책/기여자(저자) N+1 문제 해결
    @Query(
            "SELECT bc " +
            "FROM BookContributor bc " +
            "JOIN FETCH bc.contributor c " +
            "WHERE bc.book.id IN :bookIds " +
            "AND bc.role = 'AUTHOR'"
    )
    List<BookContributor> findAllByBookIds(
            @Param("bookIds") List<Long> bookIds
    );
}
