package org.example.bookstoreapp.domain.book.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.QBook;
import org.example.bookstoreapp.domain.bookcontributor.entity.QBookContributor;
import org.example.bookstoreapp.domain.contributor.entity.QContributor;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Book> findTop10BySearchHistories(List<SearchHistory> histories) {

        QBook book = QBook.book;
        QBookContributor bc = QBookContributor.bookContributor;
        QContributor c = QContributor.contributor;

        NumberExpression<Long> score = Expressions.numberTemplate(Long.class, "0");

        for (SearchHistory history : histories) {
            if (history.getTitle() != null && !history.getTitle().isEmpty()) {
                score = score.add(
                        new CaseBuilder()
                                .when(book.title.lower().like("%" + history.getTitle().toLowerCase() + "%"))
                                .then(1L)
                                .otherwise(0L)
                );
            }
            if (history.getName() != null && !history.getName().isEmpty()) {
                score = score.add(
                        new CaseBuilder()
                                .when(c.name.lower().like("%" + history.getName().toLowerCase() + "%"))
                                .then(1L)
                                .otherwise(0L)
                );
            }
            if (history.getCategory() != null) {
                score = score.add(
                        new CaseBuilder()
                                .when(book.category.eq(history.getCategory()))
                                .then(1L)
                                .otherwise(0L)
                );
            }
        }

        // fetch join 포함 Top10 조회 → N+1 방지
        return queryFactory
                .selectFrom(book)
                .distinct()
                .join(book.bookContributors, bc).fetchJoin()
                .join(bc.contributor, c).fetchJoin()
                .orderBy(score.desc())
                .limit(10)
                .fetch();
    }
}