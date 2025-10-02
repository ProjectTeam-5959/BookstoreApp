package org.example.bookstoreapp.domain.library.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.bookcontributor.BookContributorRepository;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final BookContributorRepository bookContributorRepository;

    // 중복 로직 메서드 - 내 서재 가져오기 + 서재 생성(최초 1회)
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

    // 책 ID 리스트로 저자 맵 만들기 (분리)
    private Map<Long, List<String>> getBookAuthorsMap(List<Long> bookIds) {
        List<BookContributor> authors = bookContributorRepository.findAllByBookIds(bookIds);

        return authors.stream()
                .collect(
                        Collectors.groupingBy(
                                bookContributor -> bookContributor.getBook().getId(),
                                Collectors.mapping(
                                        bookContributor -> bookContributor.getContributor().getName(),
                                        Collectors.toList()
                                )
                        )
                );
    }

    // 내 서재 조회 (무한 스크롤 적용)
    public Slice<LibraryBookResponse> getMyLibrary(AuthUser authUser, Pageable pageable) {

        Library library = getLibraryOrCreate(authUser);

        Slice<LibraryBook> libraryBookSlice =
                libraryBookRepository.findByLibraryId(library.getId(), pageable);

        List<Long> bookIds = libraryBookSlice
                .stream()
                .map(libraryBook -> libraryBook.getBook().getId())
                .toList();

        Map<Long, List<String>> bookAuthorsMap = getBookAuthorsMap(bookIds);

        return libraryBookSlice.map(
                libraryBook -> LibraryBookResponse.of(
                        libraryBook,
                        bookAuthorsMap.getOrDefault(
                                libraryBook.getBook().getId(),
                                List.of())
                        )
        );
    }

    // 내 서재에 책 추가
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

    // 내 서재에 책 삭제
    public void deleteBookLibrary(AuthUser authUser, Long bookId) {

        Library library = libraryRepository.findByUserId(authUser.getId()).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_LIBRARY)
        );

        LibraryBook libraryBook = libraryBookRepository.findByLibraryIdAndBookId(library.getId(), bookId).orElseThrow(
                () -> new BusinessException(LibraryErrorCode.NOT_FOUND_BOOK_IN_LIBRARY)
        );

        libraryBook.softDelete();
    }
}
