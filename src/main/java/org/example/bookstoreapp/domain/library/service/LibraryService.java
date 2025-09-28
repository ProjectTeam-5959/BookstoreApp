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

    // 내 서재 조회 //
    // 1회 서재 생성 로직 포함되므로 readOnly = true 불가
    public LibraryResponse getMyLibrary(AuthUser authUser) {

        // 내 서재 가져오기 + 서재 생성(최초 1회만)
        // findByUserId -> Optional! => 있으면 Library 반환 / 없으면 orElseGet 구문 실행되어 서재 생성
        // orElseGet : 값이 비어 있을 때만 실행되는 대체 로직
        Library library = libraryRepository.findByUserId(authUser.getId()).orElseGet(
                () -> {
                    User user = userRepository.findById(authUser.getId()).orElseThrow(
                            () -> new BusinessException(LibraryErrorCode.NOT_FOUND_LIBRARY)
                    );
                    return libraryRepository.save(Library.of(user));
                }
        );

        // 정적 팩토리 메서드(from) 호출/반환
        return LibraryResponse.from(library);
    }

    // 내 서재에 책 추가 //
    public LibraryResponse addBookLibrary(AuthUser authUser, AddBookRequest addBookRequest) {

        // 내 서재 가져오기
        // 책 단건 조회 후 책 추가 시, 내 서재 생성(최초 1회)
        // 내 서재 조회 없이 바로 책 추가 가능!
        Library library = libraryRepository.findByUserId(authUser.getId()).orElseGet(
                () -> {
                    User user = userRepository.findById(authUser.getId()).orElseThrow(
                            () -> new BusinessException(LibraryErrorCode.NOT_FOUND_USER)
                    );
                    return libraryRepository.save(Library.of(user));
                }
        );

        // 추가 할 책 가져오기
        Book book = bookRepository.findById(addBookRequest.bookId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_BOOK)
        );

        // LibraryBook 생성해서 책 등록
        LibraryBook libraryBook = LibraryBook.of(library, book);
        library.addBook(libraryBook);

        return LibraryResponse.from(library);
    }

    // 내 서재에 책 삭제 //
    public void deleteBookLibrary(AuthUser authUser, Long bookId) {

        // 서재 가져오기
        Library library = libraryRepository.findByUserId(authUser.getId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_LIBRARY)
        );

        // 편의 메서드 호출
        library.removeBook(bookId);
    }
}
