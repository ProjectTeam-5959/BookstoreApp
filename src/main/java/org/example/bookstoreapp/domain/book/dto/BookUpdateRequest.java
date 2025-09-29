package org.example.bookstoreapp.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookstoreapp.domain.book.entity.BookCategory;

import java.time.LocalDate;

@Getter
public class BookUpdateRequest {
    @Size(max = 50)
    private String publisher;

    @Size(min = 10, max = 20)
    private String isbn; // 변경 시에도 유니크 검증 필요

    private BookCategory category;

    @Size(max = 100)
    private String title;

    private LocalDate publicationDate;
}
