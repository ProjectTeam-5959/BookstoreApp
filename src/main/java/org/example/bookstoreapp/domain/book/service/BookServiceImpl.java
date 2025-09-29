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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ContributorRepository contributorRepository;
    private final BookContributorRepository bookContributorRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 도서 검색 서비스 메서드
     * <p>
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
    @Override
    public BookSingleResponse get(Long id, Pageable pageable) {
        // todo 해결할 것 : id가 null이면 오류 발생, EntityNotFoundException 발생 시, 별도의 @ExceptionHandler 설정 없으면 500 반환. 리뷰리스트 추가
        Book book = bookRepository.findById(id).orElseThrow(() ->
                // 비즈니스 규칙: 없음 → 404 (기존 EntityNotFoundException → 500 방지)
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        /**
         * 이미 삭제된 데이터라면? 예외처리
         */
        if (book.isDeleted()) {
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND);
        }
        // 도서에 달린 리뷰 조회
        Slice<Review> reviews = reviewRepository.findByBookId(book.getId(), pageable);

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
                // todo 해결할 것 : enum 값이 잘못되면 IllegalArgumentException 발생(대안은 try-catch), 잘못된 카테고리 값 들어오면 → InvalidBookException → 500.
                throw new BusinessException(BookErrorCode.INVALID_CATEGORY); // 400 Bad Request
            }
        }

        // JPA 더티 체킹.
        return toResponse(book);
    }

    @Override
    public void delete(AuthUser authUser, Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        /**
         * 이미 삭제된 데이터라면? 예외처리
         */
        if (book.isDeleted()) {
            throw new BusinessException(BookErrorCode.BOOK_NOT_FOUND);
        }

        // 만약 책을 등록한 관리자의 아이디와 책을 등록한 관리자가 다르다면 예외처리 -> 일단 관리자끼리도 구분할 필요가 있을 지는 고민해보자!
        if (!Objects.equals(book.getCreatedBy(), authUser.getId())) {
            throw new BusinessException(BookErrorCode.DELETE_CONFLICT);
        }

        book.softDelete(); // softDelete 적용
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

    @Override
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
}
