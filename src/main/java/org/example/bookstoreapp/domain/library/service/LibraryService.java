package org.example.bookstoreapp.domain.library.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.library.dto.request.AddBookRequest;
import org.example.bookstoreapp.domain.library.dto.response.LibraryBookResponse;
import org.example.bookstoreapp.domain.library.dto.response.LibraryBookSimpleResponse;
import org.example.bookstoreapp.domain.library.entity.Library;
import org.example.bookstoreapp.domain.library.entity.LibraryBook;
import org.example.bookstoreapp.domain.library.exception.LibraryErrorCode;
import org.example.bookstoreapp.domain.library.repository.LibraryBookRepository;
import org.example.bookstoreapp.domain.library.repository.LibraryRepository;
import org.example.bookstoreapp.domain.user.entity.User;
import org.example.bookstoreapp.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    // 내 서재 조회 (무한 스크롤 적용) //
    // 1회 서재 생성 로직 포함되므로 readOnly = true 불가
    public Slice<LibraryBookResponse> getMyLibrary(AuthUser authUser, Pageable pageable) {

        Library library = getLibraryOrCreate(authUser);

        Slice<LibraryBook> libraryBookSlice = libraryBookRepository.findByLibraryId(library.getId(), pageable);

        return libraryBookSlice.map(
                LibraryBookResponse::from
        );
    }

    // 내 서재에 책 추가 //
    public LibraryBookSimpleResponse addBookLibrary(AuthUser authUser, AddBookRequest addBookRequest) {

        Library library = getLibraryOrCreate(authUser);

        Book book = bookRepository.findById(addBookRequest.bookId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_BOOK)
        );

        LibraryBook libraryBook = libraryBookRepository.findEvenDeleted(library.getId(), book.getId());

        if (libraryBook == null) {
            libraryBook = LibraryBook.of(library, book);
        } else if (libraryBook.isDeleted()) {
            libraryBook.restore();
        } else {
            throw new BusinessException(LibraryErrorCode.ALREADY_EXIST_BOOK);
        }

        library.addBook(libraryBook);

        return  LibraryBookSimpleResponse.from(libraryBook);
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
