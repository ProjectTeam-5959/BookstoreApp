package org.example.bookstoreapp.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.book.exception.BookErrorCode;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.book.repository.BookSpecs;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    /**
     * 도서 검색 서비스 메서드
     *
     * - title, category, publisher 조건을 Specification 으로 동적 조합.
     * - 입력값이 null 또는 빈 값이면 해당 조건은 무시됨.
     * - 예: title + category만 검색 / category + publisher만 검색 가능.
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String title, String category, String publisher) {
        // 기본 조건: titleContains
        Specification<Book> spec = BookSpecs.titleContains(title);
        // 'where(org.springframework.data.jpa.domain.Specification<T>)'은(는) 버전 3.5.0 이상에서 지원 중단되며 제거될 예정
//        Specification<Book> spec = Specification.where(BookSpecs.titleContains(title));

        // and 조건으로 category, publisher 추가
        spec = spec.and(BookSpecs.categoryEq(category))
                .and(BookSpecs.publisherEq(publisher));

        // Repository를 통해 실행 → 조건에 맞는 도서 목록 반환
        return bookRepository.findAll(spec);
    }

    @Override
    public BookResponse create(BookCreateRequest req, Long userId) {
        if (bookRepository.existsByIsbn(req.getIsbn())) {
            // todo 해결할 것 : DB 조회 과정에서 오류가 나면 500
            // 비즈니스 규칙: 중복 → 409
            throw new BusinessException(BookErrorCode.DUPLICATE_ISBN);
        }
        BookCategory category;
        try {
            category = BookCategory.valueOf(req.getCategory());
        } catch (IllegalArgumentException e) {
            // 비즈니스 규칙: 잘못된 카테고리 → 400
            throw new BusinessException(BookErrorCode.INVALID_CATEGORY);
        }

        Book book = Book.builder()
                .publisher(req.getPublisher())
                .isbn(req.getIsbn())
                .category(category)
                .title(req.getTitle())
                .publicationDate(req.getPublicationDate())
                .createdBy(userId)
                .build();

        return toResponse(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public BookResponse get(Long id) {
        // todo 해결할 것 : id가 null이면 오류 발생, EntityNotFoundException 발생 시, 별도의 @ExceptionHandler 설정 없으면 500 반환.
        Book book = bookRepository.findById(id).orElseThrow(() ->
                // 비즈니스 규칙: 없음 → 404 (기존 EntityNotFoundException → 500 방지)
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookResponse> search(String title, String category, String publisher, String isbn, Pageable pageable) {
        // todo 해결할 것 : pageable 이 null 이면 오류 발생, BookSpecs 조합 과정이나 JPA 실행 중 오류 발생 시 500 발생.
        // BookSpecs.categoryEq(category) 내부에서 BookCategory.valueOf(category) 같은 변환이 있으면 500 위험
        Specification<Book> spec = BookSpecs.titleContains(title)
                .and(BookSpecs.categoryEq(category))
                .and(BookSpecs.publisherEq(publisher))
                .and(BookSpecs.isbnEq(isbn));
        return bookRepository
                .findAll(spec, pageable)
                .map(this::toResponse);
    }

    @Override
    public BookResponse update(Long id, BookUpdateRequest req) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                // todo 해결할 것 : id가 null이면 오류 발생, findById에서 못 찾으면 → EntityNotFoundException → 500.
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        if (req.getIsbn() != null && !req.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existsByIsbn(req.getIsbn())) {
                // todo 해결할 것 : DB 조회 과정에서 오류가 나면 500, ISBN 중복 검사 시 → DataIntegrityViolationException → 500.
                throw new BusinessException(BookErrorCode.DUPLICATE_ISBN); // 409 Conflict
            }
            book.changeIsbn(req.getIsbn());
        }
        if (req.getPublisher() != null)
            book.changePublisher(req.getPublisher()
            );

        if (req.getTitle() != null)
            book.changeTitle(req.getTitle()
            );

        if (req.getPublicationDate() != null)
            book.changePublicationDate(req.getPublicationDate()
            );

        if (req.getCategory() != null) {
            try {
                book.changeCategory(BookCategory.valueOf(req.getCategory()));
            } catch (IllegalArgumentException e) {
                // 잘못된 카테고리 값 처리 (예: 예외 던지기 또는 무시)
                // todo 해결할 것 : enum 값이 잘못되면 IllegalArgumentException 발생(대안은 try-catch), 잘못된 카테고리 값 들어오면 → InvalidBookException → 500.
                throw new BusinessException(BookErrorCode.INVALID_CATEGORY); // 400 Bad Request
            }
        }

        // JPA 더티 체킹.
        return toResponse(book);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            // todo 해결할 것 : id가 null이면 오류 발생, DB 조회 과정에서 존재하지 않으면 EntityNotFoundException → 500
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND); // 404 not found
        }
        // todo 해결할 것 : id가 null이면 오류 발생, DB 삭제 과정에서 오류 발생 시 500, DB delete 중 제약 조건(예: 외래키) 위반되면 DataIntegrityViolationException → 500
        try {
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // 비즈니스 규칙: FK 등 제약으로 삭제 불가 → 409 conflict
            throw new BusinessException(BookErrorCode.DELETE_CONFLICT);
        }
    }

    private BookResponse toResponse(Book b) {
        return BookResponse.builder()
                .id(b.getId())
                .publisher(b.getPublisher())
                .isbn(b.getIsbn())
                .category(String.valueOf(b.getCategory()))
                .title(b.getTitle())
                .publicationDate(b.getPublicationDate())
                .createdBy(b.getCreatedBy())
                .createdAt(b.getCreatedAt())
                .modifiedAt(b.getModifiedAt())
                .build();
    }
}
