package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.library.entity.LibraryBook;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LibraryBookSimpleResponse {

    private final Long bookId;
    private final String title;
    private final LocalDateTime addedAt;

    // 정적 팩토리 메서드 -> 등록 응답용 (저자 제외)
    public static LibraryBookSimpleResponse from(LibraryBook libraryBook) {

        return new LibraryBookSimpleResponse(
                libraryBook.getBook().getId(),
                libraryBook.getBook().getTitle(),
                libraryBook.getAddedAt()
        );
    }
}
