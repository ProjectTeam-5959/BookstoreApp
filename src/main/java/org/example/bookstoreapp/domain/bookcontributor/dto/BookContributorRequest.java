package org.example.bookstoreapp.domain.bookcontributor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookContributorRequest {

    @NotNull
    private ContributorRole role;
}
