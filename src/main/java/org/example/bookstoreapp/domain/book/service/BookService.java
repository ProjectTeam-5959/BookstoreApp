package org.example.bookstoreapp.domain.book.service;

import org.example.bookstoreapp.domain.book.dto.BookCreateRequest;
import org.example.bookstoreapp.domain.book.dto.BookResponse;
import org.example.bookstoreapp.domain.book.dto.BookUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponse create(
            BookCreateRequest request, Long userId
    );

    BookResponse get(
            Long id
    );

    Page<BookResponse> search(
            String title, String category, String publisher, String isbn, Pageable pageable
    );

    BookResponse update(
            Long id, BookUpdateRequest request
    );

    void delete(
            Long id
    );
}
