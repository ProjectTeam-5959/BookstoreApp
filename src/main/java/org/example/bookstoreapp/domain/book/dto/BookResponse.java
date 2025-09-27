package org.example.bookstoreapp.domain.book.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String publisher;
    private String isbn;
    private String category;
    private String title;
    private LocalDate publicationDate;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    private BookResponse(Long id,
                         String publisher, String isbn, String category, String title, Long createdBy,
                         LocalDate publicationDate, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
