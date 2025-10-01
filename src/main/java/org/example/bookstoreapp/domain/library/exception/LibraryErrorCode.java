package org.example.bookstoreapp.domain.library.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookstoreapp.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LibraryErrorCode implements ErrorCode {

    NOT_FOUND_LIBRARY(HttpStatus.NOT_FOUND, "존재하지 않는 서재입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "존재하지 않는 책입니다."),
    NOT_FOUND_BOOK_IN_LIBRARY(HttpStatus.NOT_FOUND, "내 서재에 존재하지 않는 책입니다."),
    ALREADY_EXIST_BOOK(HttpStatus.CONFLICT,  "이미 서재에 존재하는 책입니다.");  // 409 중복 에러

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
