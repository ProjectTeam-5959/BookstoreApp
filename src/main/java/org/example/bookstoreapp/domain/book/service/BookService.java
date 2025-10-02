package org.example.bookstoreapp.domain.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

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
        Book book = bookRepository.findByIdAndDeletedFalse(bookId).orElseThrow(() ->
                new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        if (req.getIsbn() != null) {
            if (req.getIsbn().equals(book.getIsbn())) {
                throw new BusinessException(BookErrorCode.INVALID_ISBN);
            }
            if (bookRepository.existsByIsbn(req.getIsbn())) {
                throw new BusinessException(BookErrorCode.DUPLICATE_ISBN);
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
                throw new BusinessException(BookErrorCode.INVALID_CATEGORY);
            }
        }

        return toResponse(book);
    }

    public void delete(AuthUser authUser, Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new BusinessException(BookErrorCode.BOOK_NOT_FOUND)
        );

        if (!Objects.equals(book.getCreatedBy(), authUser.getId())) {
            throw new BusinessException(BookErrorCode.FORBIDDEN_ACCESS_BOOK);
        }

        int deletedReviewCount = reviewRepository.softDeleteByBookId(id);

        log.info("삭제된 리뷰의 개수: {}", deletedReviewCount);

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
        Book book = bookRepository.findByIdAndDeletedFalse(bookId).orElseThrow(() ->
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
