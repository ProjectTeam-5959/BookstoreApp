package org.example.bookstoreapp.domain.book.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "도서를 찾을 수 없습니다."),
    DUPLICATE_ISBN(HttpStatus.CONFLICT, "이미 존재하는 ISBN 입니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    INVALID_SEARCH_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리 검색값입니다."),

    // 기여자 소프트 딜리트 관련
    DELETE_CONFLICT(HttpStatus.CONFLICT, "연관 데이터로 인해 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}