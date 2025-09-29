package org.example.bookstoreapp.domain.searchHistory.dto.response;

import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.searchHistory.entity.SearchHistory;

import java.time.LocalDateTime;

@Getter
public class MySearchHistoryResponse {

    private final Long id;
    private final String title;
    private final String name;
    private final BookCategory category;
    private final LocalDateTime createdAt;

    public MySearchHistoryResponse(
            Long id,
            String title,
            String name,
            BookCategory category,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
    }

    public static MySearchHistoryResponse from(SearchHistory searchHistory) {
        return new MySearchHistoryResponse(
                searchHistory.getId(),
                searchHistory.getTitle(),
                searchHistory.getName(),
                searchHistory.getCategory(),
                searchHistory.getCreatedAt()
        );
    }
}
