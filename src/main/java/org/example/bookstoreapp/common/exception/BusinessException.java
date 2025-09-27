package org.example.bookstoreapp.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
// BusinessException을 `ErrorCode` 기반으로 변경
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }
}
