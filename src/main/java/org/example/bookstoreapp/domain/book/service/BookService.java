package org.example.bookstoreapp.domain.book.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.auth.dto.AuthUser;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookSingleResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.exception.BookErrorCode;
import org.example.bookstoreapp.domain.book.repository.BookRepository;
import org.example.bookstoreapp.domain.book.repository.BookSpecs;
import org.example.bookstoreapp.domain.bookcontributor.BookContributorRepository;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorRequest;
import org.example.bookstoreapp.domain.bookcontributor.dto.BookContributorResponse;
import org.example.bookstoreapp.domain.bookcontributor.entity.BookContributor;
import org.example.bookstoreapp.domain.contributor.entity.Contributor;
import org.example.bookstoreapp.domain.contributor.repository.ContributorRepository;
import org.example.bookstoreapp.domain.review.dto.response.ReviewResponse;
import org.example.bookstoreapp.domain.review.entity.Review;
import org.example.bookstoreapp.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final ContributorRepository contributorRepository;
    private final BookContributorRepository bookContributorRepository;
    private final ReviewRepository reviewRepository;

    public BookResponse create(BookCreateRequest req, Long userId) {
        if (bookRepository.existsByIsbn(req.getIsbn())) {
            throw new BusinessException(BookErrorCode.DUPLICATE_ISBN);  // 예외규칙 : 중복 → 409
        }

        Book book = Book.builder()
                .publisher(req.getPublisher())
                .isbn(req.getIsbn())
                .category(req.getCategory())
                .title(req.getTitle())
                .publicationDate(req.getPublicationDate())
                .createdBy(userId)
                .build();

        return toResponse(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public BookSingleResponse get(Long id, Long lastReviewId, LocalDateTime lastModifiedAt, int size) {
        Book book = bookRepository.findByIdAndDeletedFalse(id).orElseThrow(() ->
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)     // 예외 규칙: 없음 → 404 (기존 EntityNotFoundException → 500 방지)
        );

        // 도서에 달린 리뷰 조회
        Slice<Review> reviews = reviewRepository.findByBookId(book.getId(), lastReviewId, lastModifiedAt, size);

        return new BookSingleResponse(
                book.getId(),
                book.getPublisher(),
                book.getIsbn(),
                book.getCategory(),
                book.getTitle(),
                book.getPublicationDate(),
                book.getCreatedBy(),
                book.getCreatedAt(),
                book.getModifiedAt(),
                reviews.map(ReviewResponse::from)
        );
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(String title, String category, String publisher, String isbn, Pageable pageable) {
        Specification<Book> spec = BookSpecs.titleContains(title)
                .and(BookSpecs.categoryEq(category))
                .and(BookSpecs.publisherEq(publisher))
                .and(BookSpecs.isbnEq(isbn));
        return bookRepository
                .findAll(spec, pageable)
                .map(this::toResponse);
    }

    public BookResponse update(Long bookId, BookUpdateRequest req) {
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        if (req.getIsbn() != null) {
            if (req.getIsbn().equals(book.getIsbn())) {
                throw new BusinessException(BookErrorCode.INVALID_ISBN);
            }
            if (bookRepository.existsByIsbn(req.getIsbn())) {
                throw new BusinessException(BookErrorCode.DUPLICATE_ISBN); // 409 Conflict
            }
            book.changeIsbn(req.getIsbn());
        }
        if (req.getPublisher() != null)
            book.changePublisher(req.getPublisher());

        if (req.getTitle() != null)
            book.changeTitle(req.getTitle());

        if (req.getPublicationDate() != null)
            book.changePublicationDate(req.getPublicationDate());

        if (req.getCategory() != null) {
            try {
                book.changeCategory(req.getCategory());
            } catch (IllegalArgumentException e) {
                // 잘못된 카테고리 값 처리 (예: 예외 던지기 또는 무시)
                throw new BusinessException(BookErrorCode.INVALID_CATEGORY); // 400 Bad Request
            }
        }

        // JPA 더티 체킹.
        return toResponse(book);
    }

    public void delete(AuthUser authUser, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        /** 이미 삭제된 데이터라면? 예외처리 */
        if (book.isDeleted()) {
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND);
        }

        // 만약 책을 등록한 관리자의 아이디와 책을 등록한 관리자가 다르다면 예외처리 -> 일단 관리자끼리도 구분할 필요가 있을 지는 고민해보자!
        if (!Objects.equals(book.getCreatedBy(), authUser.getId())) {
            throw new BusinessException(BookErrorCode.FORBIDDEN_ACCESS_BOOK);
        }

        // softDelete 적용
        book.softDelete();
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

    public BookContributorResponse linkContributor(
            Long bookId,
            Long contributorId,
            BookContributorRequest request
    ) {
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND));

        Contributor contributor = contributorRepository.findById(contributorId).orElseThrow(
                () -> new BusinessException(BookErrorCode.CONTRIBUTOR_NOT_FOUND)
        );

        BookContributor bookContributor = new BookContributor(book, contributor, request.getRole());

        bookContributorRepository.save(bookContributor);

        return new BookContributorResponse(
                bookContributor.getBook().getId(),
                bookContributor.getContributor().getId(),
                bookContributor.getRole()
        );
    }

    /**
     * 도서 검색 서비스 메서드
     * <p>
     * - title, category, publisher 조건을 Specification 으로 동적 조합.
     * - 입력값이 null 또는 빈 값이면 해당 조건은 무시됨.
     * - 예: title + category만 검색 / category + publisher만 검색 가능.
     */
//    @Transactional(readOnly = true)
//    public List<Book> searchBooks(String title, String category, String publisher) {
//        // 기본 조건: titleContains
//        Specification<Book> spec = BookSpecs.titleContains(title);
//        // 'where(org.springframework.data.jpa.domain.Specification<T>)'은(는) 버전 3.5.0 이상에서 지원 중단되며 제거될 예정
////        Specification<Book> spec = Specification.where(BookSpecs.titleContains(title));
//
//        // and 조건으로 category, publisher 추가
//        spec = spec.and(BookSpecs.categoryEq(category))
//                .and(BookSpecs.publisherEq(publisher));
//
//        // Repository를 통해 실행 → 조건에 맞는 도서 목록 반환
//        return bookRepository.findAll(spec);
//    }
}
