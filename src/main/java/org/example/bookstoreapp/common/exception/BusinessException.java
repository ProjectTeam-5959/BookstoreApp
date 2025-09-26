package org.example.bookstoreapp.common.exception;

// BusinessException을 `ErrorCode` 기반으로 변경
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return errorCode.getStatus();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
