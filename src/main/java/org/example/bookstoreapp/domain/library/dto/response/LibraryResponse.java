package org.example.bookstoreapp.domain.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.contributor.entity.ContributorRole;
import org.example.bookstoreapp.domain.library.entity.Library;

import java.util.List;

@Getter
@AllArgsConstructor
public class LibraryResponse {

    // 내 서재에 담긴 책 목록 (LibraryBook 기준) //
    private List<LibraryBookResponse> books;

    // 정적 팩토리 메서드(엔티티 -> DTO 변환용) //
    // LibraryBook -> Book -> Author 관계 따라가며 데이터 추출
    public static LibraryResponse from(Library library){

        // Library에 연결된 LibraryBook 목록 가져오기
        List<LibraryBookResponse> bookResponses = library.getLibraryBooks().stream()
                // 각각의 LibraryBook 에 대해 연결된 책 꺼냄
                .map(libraryBook -> {
                    Book book = libraryBook.getBook();

                    // 저자(AUTHOR) 역할만 필터링해서 이름 리스트 생성
                    List<String> authors = book.getBookContributors().stream()
                            .filter(bookContributor -> bookContributor.getRole() == ContributorRole.AUTHOR)
                            .map(bookContributor -> bookContributor.getContributor().getName())
                            .toList();

                    // LibraryBookResponse 생성 (책 정보 + 저자 + 서재에 책 추가된 날짜)
                    return new LibraryBookResponse(
                            book.getId(),
                            book.getTitle(),
                            authors,
                            libraryBook.getAddedAt()
                    );
                })
                // LibraryBookResponse 들을 리스트로 모아 bookResponse 에 저장
                .toList();
        // bookResponse 리스트 이용해 새로운 LibraryResponse 반환
        return new LibraryResponse(bookResponses);
    }
}
