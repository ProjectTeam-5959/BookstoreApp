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

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements CustomBookRepository {

    private static final long MATCH_SCORE = 1L;
    private static final long NO_SCORE = 0L;
    private static final int TOP_N = 10;
    
    private final JPAQueryFactory queryFactory;

    @Override
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
                                .then(MATCH_SCORE)
                                .otherwise(NO_SCORE)
                );
            }
            if (history.getName() != null && !history.getName().isEmpty()) {
                score = score.add(
                        new CaseBuilder()
                                .when(c.name.lower().like("%" + history.getName().toLowerCase() + "%"))
                                .then(MATCH_SCORE)
                                .otherwise(NO_SCORE)
                );
            }
            if (history.getCategory() != null) {
                score = score.add(
                        new CaseBuilder()
                                .when(book.category.eq(history.getCategory()))
                                .then(MATCH_SCORE)
                                .otherwise(NO_SCORE)
                );
            }
        }

        // Top 10 Book ID 먼저 조회 (DB에서 limit 적용)
        List<Long> top10BookIds = queryFactory
                .select(book.id)
                .from(book)
                .join(book.bookContributors, bc)
                .join(bc.contributor, c)
                .orderBy(score.desc())
                .limit(TOP_N)
                .fetch();

        if (top10BookIds.isEmpty()) {
            return Collections.emptyList();
        }

        // fetch join으로 연관 엔티티 포함하여 최종 조회
        return queryFactory
                .selectFrom(book)
                .distinct()
                .join(book.bookContributors, bc).fetchJoin()
                .join(bc.contributor, c).fetchJoin()
                .where(book.id.in(top10BookIds))
                .fetch();
    }
}
