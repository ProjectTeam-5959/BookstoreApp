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

    // 책 ID 리스트로 저자 맵 만들기 (분리)
    private Map<Long, List<String>> getBookAuthorsMap(List<Long> bookIds) {
        // Repository 에서 JPQL 쿼리를 통해 'AUTHOR' 작가만 거름
        List<BookContributor> authors = bookContributorRepository.findAllByBookIds(bookIds);

        // 조회된 작가 리스트를 stream 으로 변환
        return authors.stream()
                // stream 결과 수집 -> groupingBy 사용하여 책 id 별로 그룹핑
                .collect(
                        Collectors.groupingBy(
                                // 그룹핑의 기준!
                                // -> BookContributor 에서 연결된 책 id 꺼내 key(책 ID)로 사용
                                bookContributor -> bookContributor.getBook().getId(),
                                // 그룹핑된 값 가공
                                // 각 BookContributor 에서 Contributor(작가)의 name 만 뽑아서 List 형태로 모음
                                // => Map<Long, List<String>> 구조 완성
                                Collectors.mapping(
                                        bookContributor -> bookContributor.getContributor().getName(),
                                        Collectors.toList()
                                )
                        )
                );
    }

    // 내 서재 조회 (무한 스크롤 적용) //
    // 1회 서재 생성 로직 포함되므로 readOnly = true 불가
    public Slice<LibraryBookResponse> getMyLibrary(AuthUser authUser, Pageable pageable) {

        // 로그인 유저의 서재 가져오기 (없으면 생성 후 반환)
        Library library = getLibraryOrCreate(authUser);

        // 페이징 적용된 Slice 형태로 조회 -> limit + offset 기반
        Slice<LibraryBook> libraryBookSlice =
                libraryBookRepository.findByLibraryId(library.getId(), pageable);

        // LibraryBook 객체들에서 Book의 ID만 추출하여 리스트 생성
        List<Long> bookIds = libraryBookSlice
                .stream()
                .map(libraryBook -> libraryBook.getBook().getId())
                .toList();

        // getBookAuthorsMap (저자 맵) 메서드 호출
        Map<Long, List<String>> bookAuthorsMap = getBookAuthorsMap(bookIds);

        return libraryBookSlice.map(
                libraryBook -> LibraryBookResponse.of(
                        // 기본 도서정보
                        libraryBook,
                        // 현재 도서 id에 해당하는 저자 리스트 (없으면 빈 리스트)
                        // V getOrDefault(Object key, V defaultValue)
                        // - key : 찾고 싶은 키
                        // - defaultValue : 키 없을 때 대체 반환값
                        bookAuthorsMap.getOrDefault(
                                libraryBook.getBook().getId(),
                                List.of())
                        )
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
