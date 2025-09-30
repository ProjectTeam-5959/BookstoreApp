package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;
import org.example.bookstoreapp.domain.library.entity.LibraryBook;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LibraryBookResponse {

    private Long bookId;
    private String title;
    private List<String> authors;
    private LocalDateTime addedAt;

    // 정적 팩토리 메서드
    public static LibraryBookResponse from(LibraryBook libraryBook) {

        Book book = libraryBook.getBook();

        List<String> authors = book.getBookContributors().stream()
                .filter(bookContributor -> bookContributor.getRole() == ContributorRole.AUTHOR)
                .map(bookContributor -> bookContributor.getContributor().getName())
                .collect(Collectors.toList());

        return new LibraryBookResponse(
                book.getId(),
                book.getTitle(),
                authors,
                libraryBook.getAddedAt()
        );
    }
}
