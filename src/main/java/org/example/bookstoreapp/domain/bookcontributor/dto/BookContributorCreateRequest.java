package org.example.bookstoreapp.domain.bookcontributor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookContributorCreateRequest {
    @NotNull
    private Long bookId;

    @NotNull
    private Long authorId;

    @NotBlank
    @Size(max = 20)
    private ContributorRole role;
}
