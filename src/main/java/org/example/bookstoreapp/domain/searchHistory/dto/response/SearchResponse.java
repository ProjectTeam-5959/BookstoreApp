package org.example.bookstoreapp.domain.searchHistory.dto.response;

import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.example.bookstoreapp.domain.contributor.dto.SearchContributorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchResponse {

    private final Long id;
    private final String title;
    private final List<SearchContributorResponse> contributors;
    private final BookCategory category;
    private final LocalDateTime createdAt;

    public SearchResponse(
            Long id,
            String title,
            List<SearchContributorResponse> contributors,
            BookCategory category,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.contributors = contributors;
        this.category = category;
        this.createdAt = createdAt;
    }

    public static SearchResponse from(Book book) {
        List<SearchContributorResponse> contributors = new ArrayList<>();
        for (BookContributor bc : book.getBookContributors()) {
            contributors.add(SearchContributorResponse.from(bc.getContributor(), bc.getRole()));
        }
        return new SearchResponse(
                book.getId(),
                book.getTitle(),
                contributors,
                book.getCategory(),
                book.getCreatedAt()
        );
    }
}
