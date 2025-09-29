package org.example.bookstoreapp.domain.book.entity;

import org.example.bookstoreapp.common.exception.BusinessException;
import org.example.bookstoreapp.domain.book.exception.BookErrorCode;

public enum BookCategory {  // 도서 카테고리
    NOVEL,
    ESSAY,
    COMPUTER_IT,
    HUMANITIES,
    ECONOMICS_BUSINESS,
    SELF_DEVELOPMENT,
    SCIENCE,
    HISTORY,
    ART,
    CHILDREN,
    OTHER;
    // 소설, 에세이, 컴퓨터/IT, 인문학, 경제/경영, 자기계발, 과학, 역사, 예술, 아동, 기타

    public static BookCategory from(String category) {
        if (category == null || category.isBlank()) {
            return null;
        } try {
            return BookCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(BookErrorCode.INVALID_SEARCH_CATEGORY);
        }
    }
}
