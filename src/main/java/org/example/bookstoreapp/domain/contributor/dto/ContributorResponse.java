package org.example.bookstoreapp.domain.contributor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContributorResponse {
    private final Long id;
    private final String name;
}
