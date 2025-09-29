package org.example.bookstoreapp.domain.library.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.library.dto.request.AddBookRequest;
import org.example.bookstoreapp.domain.library.dto.response.LibraryResponse;
import org.example.bookstoreapp.domain.library.entity.Library;
import org.example.bookstoreapp.domain.library.entity.LibraryBook;
import org.example.bookstoreapp.domain.library.exception.LibraryErrorCode;
import org.example.bookstoreapp.domain.library.repository.LibraryBookRepository;
import org.example.bookstoreapp.domain.library.repository.LibraryRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LibraryBookRepository libraryBookRepository;

    // 중복 로직 메서드 - 내 서재 가져오기 + 서재 생성(최초 1회)
    // findByUserId -> Optional! => 있으면 Library 반환 / 없으면 orElseGet 구문 실행되어 서재 생성
    // orElseGet : 값이 비어 있을 때만 실행되는 대체 로직
    private Library getLibraryOrCreate(AuthUser authUser) {
        return libraryRepository.findByUserId(authUser.getId()).orElseGet(
                () -> {
                    User user = userRepository.findById(authUser.getId()).orElseThrow(
                            () -> new BusinessException(LibraryErrorCode.NOT_FOUND_USER)
                    );
                    return libraryRepository.save(Library.of(user));
                }
        );
    }

    // 내 서재 조회 //
    // 1회 서재 생성 로직 포함되므로 readOnly = true 불가
    public LibraryResponse getMyLibrary(AuthUser authUser) {

        // 내 서재 가져오기(+ 최초 1회만 내 서재 생성)
        Library library = getLibraryOrCreate(authUser);

        // 정적 팩토리 메서드(from) 호출/반환
        return LibraryResponse.from(library);
    }

    // 내 서재에 책 추가 //
    public LibraryResponse addBookLibrary(AuthUser authUser, AddBookRequest addBookRequest) {

        // 내 서재 가져오기(+ 최초 1회만 내 서재 생성)
        Library library = getLibraryOrCreate(authUser);

        // 추가할 책 가져오기
        Book book = bookRepository.findById(addBookRequest.bookId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_BOOK)
        );

        // 기존 LibraryBook 에서 책 찾기 (삭제 포함)
        LibraryBook libraryBook = libraryBookRepository.findEvenDeleted(library.getId(), book.getId());

        // null 일 때 책 생성
        if (libraryBook == null) {
            libraryBook = LibraryBook.of(library, book);
        // 삭제된 책 복구 (deleted = false) -> 삭제된 책도 등록 가능!
        } else if (libraryBook.isDeleted()) {
            libraryBook.restore();
        // 중복 에러 처리(409)
        } else {
            throw new BusinessException(LibraryErrorCode.ALREADY_EXIST_BOOK);
        }

        // 책 추가
        // 이 과정이 없으면 테이블에 반영 안됨
        library.addBook(libraryBook);

        return LibraryResponse.from(library);
    }

    // 내 서재에 책 삭제 //
    public void deleteBookLibrary(AuthUser authUser, Long bookId) {

        // 서재 가져오기
        Library library = libraryRepository.findByUserId(authUser.getId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_LIBRARY)
        );

        // 해당 책 가져오기
        LibraryBook libBook = library.getLibraryBooks().stream()
                .filter(libraryBook -> libraryBook.getBook().getId().equals(bookId))
                // 위 경우 타입 : Stream<LibraryBook>
                // findFirst() 역할 : 첫 번째 요소 꺼내 Optional<LibraryBook> 로 반환
                // orElseThrow 가능해짐! (없으면 타입 불일치로 불가능!)
                .findFirst()
                .orElseThrow(
                        () -> new BusinessException(LibraryErrorCode.NOT_FOUND_BOOK_IN_LIBRARY)
                );

        libBook.softDelete();
    }
}
