package org.example.bookstoreapp.domain.bookcontributor.dto;

import lombok.Getter;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Getter
public class BookContributorResponse {

    private final Long bookId;
    private final Long contributorId;
    private final ContributorRole role;

    public BookContributorResponse(Long bookId, Long contributorId, ContributorRole role) {
        this.bookId = bookId;
        this.contributorId = contributorId;
        this.role = role;
    }
}
