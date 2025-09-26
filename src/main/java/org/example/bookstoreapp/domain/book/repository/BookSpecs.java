package org.example.bookstoreapp.domain.book.repository;

import org.example.bookstoreapp.domain.book.entity.Book;
import org.springframework.data.jpa.domain.Specification;

/**
 * Book 엔티티에 대한 동적 검색 조건(Specification) 정의.
 * 1. JpaSpecificationExecutor<Book> 와 함께 사용 → 동적 검색 가능.
 * 2. 검색 조건을 별도의 Specification 메서드로 정의 (제목/카테고리/출판사/ISBN).
 * 3. titleContains → 부분 검색(LIKE) + 소문자 변환(lower)으로 대소문자 무시.
 * 4. null 또는 빈 값이 들어오면 null 반환 → 해당 조건은 무시됨.
 * 5. 여러 조건을 조합 가능 (Specification.where(...).and(...).or(...)).
 * */

public class BookSpecs {

    /**
     * 제목(title)에 특정 키워드가 포함되어 있는지 검색.
     * - LIKE 검색으로 부분 일치 지원.
     * - cb.lower(...) + toLowerCase() → 대소문자 구분 없이 검색 가능.
     * - keyword 가 null 또는 blank 면 null 반환 → 조건 제외.
     * */
    public static Specification<Book> titleContains(String keyword) {
        return (root, query, cb)
                -> keyword == null || keyword.isBlank() ? null :
                cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }

    /**
     * 카테고리(category)가 일치하는지 검색.
     * - 정확히 같은 값 비교 (Equal).
     * - category 가 null 또는 blank 면 null 반환 → 조건 제외.
     * */
    public static Specification<Book> categoryEq(String category) {
        return (root, query, cb)
                -> category == null || category.isBlank() ? null :
                cb.equal(root.get("category"), category);
    }

    /***
     * 출판사(publisher)가 일치하는지 검색.
     * - 정확히 같은 값 비교 (Equal).
     * - publisher 가 null 또는 blank 면 null 반환 → 조건 제외.
     * */
    public static Specification<Book> publisherEq(String publisher) {
        return (root, query, cb)
                -> publisher == null || publisher.isBlank() ? null :
                cb.equal(root.get("publisher"), publisher);
    }

    /**
     * ISBN(isbn)이 일치하는지 검색.
     * - 정확히 같은 값 비교 (Equal).
     * - isbn 가 null 또는 blank 면 null 반환 → 조건 제외.
     * */
    public static Specification<Book> isbnEq(String isbn) {
        return (root, query, cb)
                -> isbn == null || isbn.isBlank() ? null :
                cb.equal(root.get("isbn"), isbn);
    }
}
