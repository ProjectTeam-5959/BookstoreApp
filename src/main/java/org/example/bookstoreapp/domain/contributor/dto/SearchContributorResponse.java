package org.example.bookstoreapp.domain.contributor.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Getter
@Builder
public class SearchContributorResponse {

    private final Long id;
    private final String name;

    // 역할도 출력할 수 있도록
    private final ContributorRole role;
}
