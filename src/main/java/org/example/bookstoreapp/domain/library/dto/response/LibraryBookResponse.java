package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class LibraryBookResponse {

    private Long bookId;
    private String title;
    private List<String> authors;
    private String publisher;
    private String isbn;
    private LocalDate publicationDate;
    private LocalDateTime addedAt;
}
