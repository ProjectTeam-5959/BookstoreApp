package org.example.bookstoreapp.domain.book.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.example.bookstoreapp.domain.book.entity.Book;
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
            throw new DataIntegrityViolationException("이미 존재하는 ISBN 입니다.");
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
    public BookResponse get(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("도서를 찾을 수 없습니다."));
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookResponse> search(String title, String category, String publisher, String isbn, Pageable pageable) {
        Specification<Book> spec = BookSpecs.titleContains(title)
                .and(BookSpecs.categoryEq(category))
                .and(BookSpecs.publisherEq(publisher))
                .and(BookSpecs.isbnEq(isbn));
        return bookRepository.findAll(spec, pageable).map(this::toResponse);
    }

    @Override
    public BookResponse update(Long id, BookUpdateRequest req) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("도서를 찾을 수 없습니다."));

        if (req.getIsbn() != null && !req.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existsByIsbn(req.getIsbn())) {
                throw new DataIntegrityViolationException("이미 존재하는 ISBN 입니다.");
            }
            book.changeIsbn(req.getIsbn());
        }
        if (req.getPublisher() != null) book.changePublisher(req.getPublisher());
        if (req.getCategory() != null) book.changeCategory(req.getCategory());
        if (req.getTitle() != null) book.changeTitle(req.getTitle());
        if (req.getPublicationDate() != null) book.changePublicationDate(req.getPublicationDate());

        // JPA 더티 체킹.
        return toResponse(book);
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("도서를 찾을 수 없습니다.");
        }
        bookRepository.deleteById(id);
    }

    private BookResponse toResponse(Book b) {
        return BookResponse.builder()
                .id(b.getId())
                .publisher(b.getPublisher())
                .isbn(b.getIsbn())
                .category(b.getCategory())
                .title(b.getTitle())
                .publicationDate(b.getPublicationDate())
                .createdBy(b.getCreatedBy())
                .createdAt(b.getCreatedAt())
                .modifiedAt(b.getModifiedAt())
                .build();
    }
}
