package org.example.bookstoreapp.domain.bookcontributor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Getter
public class BookContributorRequest {

    @NotNull
    private ContributorRole role;
}
