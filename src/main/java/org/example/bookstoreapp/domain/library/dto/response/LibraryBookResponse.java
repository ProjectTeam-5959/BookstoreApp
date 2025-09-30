package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.library.entity.LibraryBook;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LibraryBookResponse {

    private final Long bookId;
    private final String title;
    private final List<String> authors;
    private final LocalDateTime addedAt;

    // 정적 팩토리 메서드
    // LibraryBook + 저자 리스트 → LibraryBookResponse 로 변환
    public static LibraryBookResponse of(LibraryBook libraryBook, List<String> authors) {

        return new LibraryBookResponse(
                libraryBook.getBook().getId(),
                libraryBook.getBook().getTitle(),
                authors,
                libraryBook.getAddedAt()
        );
    }
}
