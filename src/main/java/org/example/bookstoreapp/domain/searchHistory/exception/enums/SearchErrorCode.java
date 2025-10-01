package org.example.bookstoreapp.domain.searchHistory.exception.enums;

import org.example.bookstoreapp.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum SearchErrorCode implements ErrorCode {
    KEYWORD_EMPTY("검색 키워드를 최소 1개 이상 입력해야 합니다.", HttpStatus.BAD_REQUEST),
    BOOK_NOT_FOUND("해당하는 도서가 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND("검색 결과가 없습니다", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("인증이 필요합니다.",HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    SearchErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
