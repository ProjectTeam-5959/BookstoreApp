package org.example.bookstoreapp.domain.book.dto;

import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class BookSingleResponse {
    private final Long id;
    private final String publisher;
    private final String isbn;
    private final BookCategory category;
    private final String title;
    private final LocalDate publicationDate;
    private final Long createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final Slice<ReviewResponse> reviews;

    public BookSingleResponse(
            Long id,
            String publisher,
            String isbn,
            BookCategory category,
            String title,
            LocalDate publicationDate,
            Long createdBy,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            Slice<ReviewResponse> reviews
    ) {
        this.id = id;
        this.publisher = publisher;
        this.isbn = isbn;
        this.category = category;
        this.title = title;
        this.publicationDate = publicationDate;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.reviews = reviews;
    }
}
