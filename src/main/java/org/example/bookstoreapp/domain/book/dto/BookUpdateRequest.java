package org.example.bookstoreapp.domain.book.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookUpdateRequest {
    @Size(max = 50)
    private String publisher;

    @Size(min = 10, max = 20)
    private String isbn; // 변경 시에도 유니크 검증 필요

    @Size(max = 30)
    private String category;

    @Size(max = 100)
    private String title;

    @PastOrPresent
    private LocalDate publicationDate;
}
