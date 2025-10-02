package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.book.entity.Book;
import org.example.bookstoreapp.domain.book.entity.BookCategory;
import org.example.bookstoreapp.domain.book.exception.BookErrorCode;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> titleContains(String keyword) {
        return (root, query, cb) ->
                (keyword == null || keyword.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Book> categoryEq(String category) {
        return (root, query, cb) -> {
            if (category == null || category.isBlank()) return null;
            try {
                BookCategory cat = BookCategory.valueOf(category);
                return cb.equal(root.get("category"), cat);
            } catch (IllegalArgumentException e) {
                throw new BusinessException(BookErrorCode.INVALID_SEARCH_CATEGORY);
            }
        };
    }

    public static Specification<Book> publisherEq(String publisher) {
        return (root, query, cb) ->
                (publisher == null || publisher.isBlank())
                        ? null
                        : cb.equal(root.get("publisher"), publisher);
    }

    public static Specification<Book> isbnEq(String isbn) {
        return (root, query, cb) ->
                (isbn == null || isbn.isBlank())
                        ? null
                        : cb.equal(root.get("isbn"), isbn);
    }
}
