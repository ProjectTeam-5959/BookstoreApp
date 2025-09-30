package org.example.bookstoreapp.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.BookCategory;

import java.time.LocalDate;

@Getter
public class BookCreateRequest {

    @NotBlank
    @Size(max = 50)
    private String publisher;

    @NotBlank
    @Size(min = 10, max = 20)
    private String isbn;                    // 하이픈 허용

    @NotNull
    private BookCategory category;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    private LocalDate publicationDate;      // 출판일 형식 : yyyy-MM-dd
}
