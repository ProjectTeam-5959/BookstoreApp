package org.example.bookstoreapp.domain.book.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.BookCategory;

import java.time.LocalDate;

@Getter
public class BookUpdateRequest {
    @Size(max = 50)
    private String publisher;

    @Size(min = 10, max = 20)
    private String isbn;

    private BookCategory category;

    @Size(max = 100)
    private String title;

    private LocalDate publicationDate;
}
