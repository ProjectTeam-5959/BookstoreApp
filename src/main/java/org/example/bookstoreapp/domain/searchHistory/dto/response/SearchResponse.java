package org.example.bookstoreapp.domain.searchHistory.dto.response;

import lombok.Getter;
import org.example.bookstoreapp.domain.contributor.dto.SearchContributorResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SearchResponse {

    private final Long id;
    private final String title;
    private final List<SearchContributorResponse> contributors;
    private final String category;
    private final LocalDateTime createdAt;

    public SearchResponse(
            Long id,
            String title,
            List<SearchContributorResponse> contributors,
            String category,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.contributors = contributors;
        this.category = category;
        this.createdAt = createdAt;
    }
}
