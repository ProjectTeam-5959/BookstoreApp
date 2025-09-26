package org.example.bookstoreapp.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class BookCreateRequest {

    @NotBlank @Size(max = 50)
    private String publisher;

    @NotBlank @Size(min = 10, max = 20)
    private String isbn;                    // 하이픈 허용

    @NotBlank @Size(max = 30)
    private String category;

    @NotBlank @Size(max = 100)
    private String title;

    @PastOrPresent
    private LocalDate publicationDate;      // 출판일 형식 : yyyy-MM-dd
}
