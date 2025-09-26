package org.example.bookstoreapp.domain.contributor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContributorCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String name; // 작가명
}
